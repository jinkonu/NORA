package nora.movlog.utils.validators;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.user.Member;
import nora.movlog.repository.user.MemberRepository;
import nora.movlog.utils.dto.user.MemberDto;
import nora.movlog.utils.dto.user.MemberJoinRequestDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import static nora.movlog.utils.constant.NumberConstant.*;
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
            bindingResult.addError(new FieldError("requestDto", "nickname", TOO_LONG_NICKNAME_ERROR));

        return bindingResult;
    }
}
