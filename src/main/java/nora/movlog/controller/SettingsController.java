package nora.movlog.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.service.user.MemberService;
import nora.movlog.utils.MemberFinder;
import nora.movlog.utils.dto.user.MemberEditDto;
import nora.movlog.utils.validators.MemberValidator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.PrintWriter;

import static nora.movlog.utils.constant.StringConstant.*;

@RequiredArgsConstructor
@Slf4j
@RequestMapping(SETTINGS_URI)
@Controller
public class SettingsController {

    private final MemberService memberService;
    private final MemberValidator memberValidator;

    @GetMapping(PASSWORD_URI)
    public String changePassword(Authentication auth,
                                 Model model) {
        String loginId = MemberFinder.getUsernameFrom(auth);
        model.addAttribute("loginId", loginId);
        model.addAttribute("editDto", MemberEditDto.builder().build());
        return "changePasswordPage";
    }

    @PostMapping(PASSWORD_URI)
    public void changePassword(Authentication auth,
                               @Valid @ModelAttribute MemberEditDto editDto,
                               BindingResult bindingResult,
                               HttpServletResponse response) throws IOException {
        String loginId = MemberFinder.getUsernameFrom(auth);
        long id = memberService.findByLoginId(loginId).getId();
        if(memberValidator.validateEditPassword(editDto, bindingResult, id).hasErrors()) {
            PrintWriter out = response.getWriter();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");
            out.println("<script> alert('비밀번호 변경에 실패하였습니다.'); location.href='/settings/password' </script>");
            out.close();
        }
        else {
            memberService.edit(loginId, editDto);
            PrintWriter out = response.getWriter();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");
            out.println("<script> alert('성공적으로 변경되었습니다.'); location.href='/settings/password' </script>");
            out.close();
        }
    }

    @GetMapping(NICKNAME_URI)
    public String changeNickname(Authentication auth,
                                 Model model) {
        String loginId = MemberFinder.getUsernameFrom(auth);
        String nickname = memberService.findByLoginId(loginId).getNickname();
        model.addAttribute("loginId", loginId);
        model.addAttribute("oldNickname", nickname);
        model.addAttribute("editDto", MemberEditDto.builder().build());
        return "changeNicknamePage";
    }

    @PostMapping(NICKNAME_URI)
    public void changeNickname(Authentication auth,
                               @Valid @ModelAttribute MemberEditDto editDto,
                               BindingResult bindingResult,
                               HttpServletResponse response) throws IOException {
        String loginId = MemberFinder.getUsernameFrom(auth);
        long id = memberService.findByLoginId(loginId).getId();
        if(memberValidator.validateEditNickname(editDto, bindingResult, id).hasErrors()) {
            PrintWriter out = response.getWriter();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");
            out.println("<script> alert('닉네임 변경에 실패하였습니다.'); location.href='/settings/nickname' </script>");
            out.close();
        }
        else {
            memberService.edit(loginId, editDto);
            PrintWriter out = response.getWriter();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");
            out.println("<script> alert('성공적으로 변경되었습니다.'); location.href='/settings/nickname' </script>");
            out.close();
        }
    }
}
