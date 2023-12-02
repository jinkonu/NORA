package nora.movlog.service.auth;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.user.Member;
import nora.movlog.repository.user.MemberRepository;
import nora.movlog.service.exception.BusinessLogicException;
import nora.movlog.service.exception.ExceptionCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Random;

import static nora.movlog.utils.constant.StringConstant.*;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final MemberRepository memberRepository;

    private final MailService mailService;
    private final RedisService redisService;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    public void sendCodeToEmail(String loginId) {
        String title = "MOVLOG 이메일 인증";
        String authCode = "MOVLOG 인증 코드 " + this.createCode() + "를 입력해주세요.";
        mailService.sendEmail(loginId, title, authCode);

        // 이메일 인증 요청 시 인증 번호 Redis에 저장 ( key = "AuthCode " + Email / value = AuthCode )
        redisService.setValues(AUTH_CODE_PREFIX + loginId, authCode, Duration.ofMillis(this.authCodeExpirationMillis));
    }

    private String createCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new BusinessLogicException(ExceptionCode.NO_SUCH_ALGORITHM);
        }
    }

    public boolean verifiedCode(String loginId, String authCode) {
        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + loginId);
        return redisAuthCode.equals(authCode);
    }

    @Transactional
    public void setVerified(String loginId, Authentication auth) {
        Member member = memberRepository.findByLoginId(loginId).get();
        member.setVerified(auth);
    }
}
