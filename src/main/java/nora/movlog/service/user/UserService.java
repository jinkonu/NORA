package nora.movlog.service.user;

import lombok.RequiredArgsConstructor;
import nora.movlog.dto.user.UserJoinRequestDto;
import nora.movlog.repository.user.CommentRepository;
import nora.movlog.repository.user.LikesRepository;
import nora.movlog.repository.user.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import static nora.movlog.domain.constant.StringConstant.*;
import static nora.movlog.domain.constant.NumberConstant.*;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;
    private final BCryptPasswordEncoder encoder;



    /* CREATE */

    public void join(UserJoinRequestDto requestDto) {
        userRepository.save(requestDto.toEntity(encoder.encode(requestDto.getPassword())));
    }



    /* VALIDATION */

    // 회원가입 요청 DTO 검사
    public BindingResult joinValid(UserJoinRequestDto requestDto, BindingResult bindingResult) {
        // loginId
        if (requestDto.getLoginId().isEmpty())
            bindingResult.addError(new FieldError("reqeustDto", "loginId", NO_LOGIN_ID_ERROR));
        else if (requestDto.getLoginId().length() < MAX_LOGIN_ID_LENGTH)
            bindingResult.addError(new FieldError("requestDto", "loginId", TOO_LONG_LOGIN_ID_ERROR));
        else if (userRepository.existsByLoginId(requestDto.getLoginId()))
            bindingResult.addError(new FieldError("requestDto", "loginId", DUPLICATE_LOGIN_ID_ERROR));

        // password
        if (requestDto.getPassword().isEmpty())
            bindingResult.addError(new FieldError("requestDto", "loginId", NO_PASSWORD_ERROR));

        // passwordCheck
        if (!requestDto.getPassword().equals(requestDto.getPasswordCheck()))
            bindingResult.addError(new FieldError("requestDto", "loginId", NOT_EQUAL_PASSWORD_ERROR));

        // nickname
        if (requestDto.getNickname().isEmpty())
            bindingResult.addError(new FieldError("requestDto", "loginId", NO_NICKNAME_ERROR));
        else if (requestDto.getNickname().length() < MAX_NICKNAME_LENGTH)
            bindingResult.addError(new FieldError("requestDto", "loginId", TOOL_LONG_NICKNAME_ERROR));

        return bindingResult;
    }
}
