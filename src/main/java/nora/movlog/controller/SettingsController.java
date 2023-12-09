package nora.movlog.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.service.user.MemberService;
import nora.movlog.utils.MemberFinder;
import nora.movlog.utils.dto.user.MemberDeleteDto;
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



    // 비밀번호 변경
    @GetMapping(PASSWORD_URI)
    public String changePassword(Authentication auth,
                                 Model model) {
        String loginId = MemberFinder.getLoginId(auth);
        model.addAttribute("loginId", loginId);
        model.addAttribute("editDto", MemberEditDto.builder().build());
        return "changePasswordPage";
    }

    @PostMapping(PASSWORD_URI)
    public void changePassword(Authentication auth,
                               @Valid @ModelAttribute MemberEditDto editDto,
                               BindingResult bindingResult,
                               HttpServletResponse response) throws IOException {
        String loginId = MemberFinder.getLoginId(auth);
        long id = memberService.findByLoginId(loginId).getId();
        if(memberValidator.validateEditPassword(editDto, bindingResult, id).hasErrors()) {
            PrintWriter out = response.getWriter();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");
            out.println("<script> alert('비밀번호 변경에 실패하였습니다.'); location.href='" + SETTINGS_URI + PASSWORD_URI + "' </script>");
            out.close();
        }
        else {
            memberService.edit(loginId, editDto);
            PrintWriter out = response.getWriter();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");
            out.println("<script> alert('성공적으로 변경되었습니다.'); location.href='" + SETTINGS_URI + PASSWORD_URI + "' </script>");
            out.close();
        }
    }


    // 닉네임 변경
    @GetMapping(NICKNAME_URI)
    public String changeNickname(Authentication auth,
                                 Model model) {
        String loginId = MemberFinder.getLoginId(auth);
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
        String loginId = MemberFinder.getLoginId(auth);
        long id = memberService.findByLoginId(loginId).getId();
        if (memberValidator.validateEditNickname(editDto, bindingResult, id).hasErrors()) {
            PrintWriter out = response.getWriter();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");
            out.println("<script> alert('닉네임 변경에 실패하였습니다.'); location.href='" + SETTINGS_URI + NICKNAME_URI + "' </script>");
            out.close();
        }
        else {
            memberService.edit(loginId, editDto);
            PrintWriter out = response.getWriter();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");
            out.println("<script> alert('성공적으로 변경되었습니다.'); location.href='" + SETTINGS_URI + NICKNAME_URI + "' </script>");
            out.close();
        }
    }


    // 프로필 사진 변경
    @GetMapping(PROFILE_PIC_URI)
    public String profilePicPage(Authentication auth,
                                 Model model) {
        model.addAttribute("loginId", MemberFinder.getLoginId(auth));
        model.addAttribute("editDto", MemberEditDto.builder().build());

        return "profilepic";
    }

    @PostMapping(PROFILE_PIC_URI)
    public void changeProfilePic(Authentication auth,
                                 @ModelAttribute MemberEditDto dto,
                                 HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        if (dto.getImage().getOriginalFilename().isEmpty()) {
            out.println("<script> alert('프로필 사진 변경에 실패했습니다.'); location.href='" + SETTINGS_URI + PROFILE_PIC_URI + "' </script>");
        }

        else {
            memberService.setProfilePic(MemberFinder.getLoginId(auth), dto.getImage());
            out.println("<script> alert('프로필 사진 변경에 성공했습니다.'); location.href='" + SETTINGS_URI + PROFILE_PIC_URI + "' </script>");
        }

        out.close();
    }


    // 계정 삭제
    @GetMapping(DELETE_URI)
    public String deletePage(Authentication auth,
                             Model model) {
        String loginId = MemberFinder.getLoginId(auth);
        model.addAttribute("loginId", loginId);
        model.addAttribute("deleteDto", new MemberDeleteDto());
        return "deletePage";
    }

    @PostMapping(DELETE_URI)
    public void deletePage(Authentication auth,
                           @Valid @ModelAttribute MemberDeleteDto dto,
                           BindingResult bindingResult,
                           HttpServletResponse response) throws IOException {
        String loginId = MemberFinder.getLoginId(auth);
        long id = memberService.findByLoginId(loginId).getId();
        if (memberValidator.validateDelete(dto, bindingResult, id).hasErrors()) {
            PrintWriter out = response.getWriter();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");
            out.println("<script> alert('탈퇴에 실패하였습니다.'); location.href='" + SETTINGS_URI + DELETE_URI + "' </script>");
            out.close();
        }
        else {
            memberService.delete(loginId);
            PrintWriter out = response.getWriter();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");
            out.println("<script> alert('성공적으로 탈퇴 처리되었습니다.'); location.href='" + LOGOUT_URI + "' </script>");
            out.close();
        }
    }
}
