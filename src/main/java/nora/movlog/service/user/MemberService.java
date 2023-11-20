package nora.movlog.service.user;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.user.Member;
import nora.movlog.dto.user.MemberDto;
import nora.movlog.dto.user.MemberJoinRequestDto;
import nora.movlog.repository.user.CommentRepository;
import nora.movlog.repository.user.LikesRepository;
import nora.movlog.repository.user.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import static nora.movlog.domain.constant.StringConstant.*;
import static nora.movlog.domain.constant.NumberConstant.*;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;
    private final BCryptPasswordEncoder encoder;



    /* CREATE */

    @Transactional
    public void join(MemberJoinRequestDto requestDto) {
        memberRepository.save(requestDto.toEntity(encoder.encode(requestDto.getPassword())));
    }



    /* READ */

    public Member profile(long id) {
        return memberRepository.findById(id).get();
    }

    public Member findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId).get();
    }

    public Page<Member> findAllByNickname(String query, PageRequest pageRequest) {
        return memberRepository.findAllByNicknameContains(query, pageRequest);
    }



    /* UPDATE */

    @Transactional
    public void edit(MemberDto dto, long id) {
        Member member = memberRepository.findById(id).get();

        if (dto.getNewPassword().isBlank())
            member.edit(member.getPassword(), dto.getNickname());
        else
            member.edit(encoder.encode(dto.getNewPassword()), dto.getNickname());
    }



    /* DELETE */

    @Transactional
    public boolean delete(long id, String nowPassword) {
        Member member = memberRepository.findById(id).get();

        if (encoder.matches(nowPassword, member.getPassword())) {
            memberRepository.delete(member);
            return true;
        }

        return false;
    }


    /* VALIDATION */

    // 회원가입 요청 DTO 검사
    public BindingResult validateJoin(MemberJoinRequestDto dto, BindingResult bindingResult) {
        // loginId
        if (dto.getLoginId().isEmpty())
            bindingResult.addError(new FieldError("requestDto", "loginId", NO_LOGIN_ID_ERROR));
        else if (dto.getLoginId().length() < MAX_LOGIN_ID_LENGTH)
            bindingResult.addError(new FieldError("requestDto", "loginId", TOO_LONG_LOGIN_ID_ERROR));
        else if (memberRepository.existsByLoginId(dto.getLoginId()))
            bindingResult.addError(new FieldError("requestDto", "loginId", DUPLICATE_LOGIN_ID_ERROR));

        // password
        if (dto.getPassword().isEmpty())
            bindingResult.addError(new FieldError("requestDto", "password", NO_PASSWORD_ERROR));

        // passwordCheck
        if (!dto.getPassword().equals(dto.getPasswordCheck()))
            bindingResult.addError(new FieldError("requestDto", "passwordCheck", NOT_EQUAL_PASSWORD_ERROR));

        // nickname
        if (dto.getNickname().isEmpty())
            bindingResult.addError(new FieldError("requestDto", "nickname", NO_NICKNAME_ERROR));
        else if (dto.getNickname().length() < MAX_NICKNAME_LENGTH)
            bindingResult.addError(new FieldError("requestDto", "nickname", TOOL_LONG_NICKNAME_ERROR));

        return bindingResult;
    }

    // 회원 정보 수정 요청 DTO 검사
    public BindingResult validateEdit(MemberDto dto, BindingResult bindingResult, long id) {
        Member member = memberRepository.findById(id).get();

        // nowPassword
        if (dto.getNowPassword().isEmpty())
            bindingResult.addError(new FieldError("dto", "nowPassword", NO_NOW_PASSWORD));
        else if (!encoder.matches(dto.getNowPassword(), member.getPassword()))
            bindingResult.addError(new FieldError("dto", "nowPassword", NOT_EQUAL_PASSWORD_ERROR));

        // newPassword
        if (!dto.getNewPassword().equals(dto.getNewPasswordCheck()))
            bindingResult.addError(new FieldError("dto", "newPasswordCheck", NOT_EQUAL_PASSWORD_ERROR));

        // nickname
        if (dto.getNickname().isEmpty())
            bindingResult.addError(new FieldError("requestDto", "nickname", NO_NICKNAME_ERROR));
        else if (dto.getNickname().length() < MAX_NICKNAME_LENGTH)
            bindingResult.addError(new FieldError("requestDto", "nickname", TOOL_LONG_NICKNAME_ERROR));

        return bindingResult;
    }
}
