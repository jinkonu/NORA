package nora.movlog.utils.validators;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.user.Member;
import nora.movlog.repository.user.MemberRepository;
import nora.movlog.utils.dto.user.MemberDeleteDto;
import nora.movlog.utils.dto.user.MemberEditDto;
import nora.movlog.utils.dto.user.MemberJoinRequestDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import static nora.movlog.utils.constant.StringConstant.*;

@RequiredArgsConstructor
@Component
public class MemberValidator {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;


    // 회원가입 요청 DTO 검사
    public BindingResult validateJoin(MemberJoinRequestDto dto, BindingResult bindingResult) {
        if (memberRepository.existsByLoginId(dto.getLoginId()))
            bindingResult.addError(new FieldError("requestDto", "loginId", DUPLICATE_LOGIN_ID_ERROR));

        return bindingResult;
    }

    // 회원 정보 수정 요청 DTO 검사
    public BindingResult validateEditPassword(MemberEditDto dto, BindingResult bindingResult, long id) {
        Member member = memberRepository.findById(id).get();

        // nowPassword
        if (!encoder.matches(dto.getNowPassword(), member.getPassword())) // 현재 비밀번호 불일치
            bindingResult.addError(new FieldError("dto", "nowPassword", NOT_EQUAL_PASSWORD_ERROR));

        // newPassword
        if (encoder.matches(dto.getNewPassword(), member.getPassword())) // 새 비밀번호가 현재 비밀번호와 같음
            bindingResult.addError(new FieldError("dto", "newPassword", SAME_PASSWORD_ERROR));

        return bindingResult;
    }

    public BindingResult validateEditNickname(MemberEditDto dto, BindingResult bindingResult, long id) {
        Member member = memberRepository.findById(id).get();

        // nowPassword
        if (!encoder.matches(dto.getNowPassword(), member.getPassword()))
            bindingResult.addError(new FieldError("dto", "nowPassword", NOT_EQUAL_PASSWORD_ERROR));

        return bindingResult;
    }

    public BindingResult validateDelete(MemberDeleteDto dto, BindingResult bindingResult, long id) {
        Member member = memberRepository.findById(id).get();

        if (!encoder.matches(dto.getPassword(), member.getPassword()))
            bindingResult.addError(new FieldError("dto", "password", NOT_EQUAL_PASSWORD_ERROR));

        return bindingResult;
    }
}
