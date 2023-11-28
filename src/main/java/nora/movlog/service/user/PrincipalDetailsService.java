package nora.movlog.service.user;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.user.Member;
import nora.movlog.domain.user.PrincipalDetails;
import nora.movlog.repository.user.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static nora.movlog.utils.constant.StringConstant.*;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByLoginId(username)
                .orElseThrow(() -> {
                    return new UsernameNotFoundException(NO_SUCH_MEMBER);
                });
        return new PrincipalDetails(member);
    }
}
