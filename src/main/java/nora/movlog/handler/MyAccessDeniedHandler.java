package nora.movlog.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import nora.movlog.domain.user.Member;
import nora.movlog.repository.user.MemberRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.io.PrintWriter;

import static nora.movlog.utils.constant.StringConstant.*;

@AllArgsConstructor
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    private final MemberRepository memberRepository;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByLoginId(auth.getName()).get();
        String memberAuth = member.getMemberAuth();
        String requestURI = request.getRequestURI();

        if (memberAuth.equals(AUTH_UNVERIFIED)) {
            if (requestURI.equals(LOGIN_URI) || requestURI.equals(JOIN_URI)) {
                PrintWriter out = response.getWriter();
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/html; charset=UTF-8");
                out.println("<script> alert('이미 로그인되어 있습니다.'); location.href='" + VERIFY_URI + "' </script>");
                out.close();
            }
            else {
                PrintWriter out = response.getWriter();
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/html; charset=UTF-8");
                out.println("<script> alert('인증 후 다시 시도해 주세요.'); location.href='" + VERIFY_URI + "' </script>");
                out.close();
            }
        }
        else {
            if (requestURI.equals(LOGIN_URI) || requestURI.equals(JOIN_URI)) {
                PrintWriter out = response.getWriter();
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/html; charset=UTF-8");
                out.println("<script> alert('이미 로그인되어 있습니다.'); location.href='" + HOME_URI + "' </script>");
                out.close();
            }
            else if (requestURI.equals(VERIFY_URI) || requestURI.equals(VERIFY_URI + RESEND_URI)) {
                PrintWriter out = response.getWriter();
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/html; charset=UTF-8");
                out.println("<script> alert('이미 인증되어 있습니다.'); history.go(-1); </script>");
                out.close();
            }
        }
    }
}
