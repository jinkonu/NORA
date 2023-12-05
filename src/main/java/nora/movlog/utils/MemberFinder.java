package nora.movlog.utils;

import nora.movlog.domain.user.PrincipalDetails;
import org.springframework.security.core.Authentication;

public final class MemberFinder {

    public static String getLoginId(Authentication auth) {
        PrincipalDetails loginMember = (PrincipalDetails) auth.getPrincipal();

        return loginMember.getUsername();
    }
}
