package nora.movlog.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.io.PrintWriter;

import static nora.movlog.utils.constant.StringConstant.*;

public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        PrintWriter out = response.getWriter();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        out.println("<script> alert('로그인 후 다시 시도해 주세요'); location.href='" + LOGIN_URI + "' </script>");
        out.close();
    }
}
