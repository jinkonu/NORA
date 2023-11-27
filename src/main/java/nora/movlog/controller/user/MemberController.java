package nora.movlog.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.domain.user.PrincipalDetails;
import nora.movlog.service.user.MemberService;
import nora.movlog.service.user.PostService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static nora.movlog.utils.constant.StringConstant.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(MEMBER_URI)
@Controller
public class MemberController {
    private final MemberService memberService;
    private final PostService postService;



    // 사용자 프로필
    @GetMapping(ID_URI)
    public String profilePage(@PathVariable long id,
                              @RequestParam(defaultValue = DEFAULT_SEARCH_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_SEARCH_SIZE) int size,
                              Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PrincipalDetails loginMember = (PrincipalDetails) principal;
        model.addAttribute("loginMember", memberService.findByLoginId(loginMember.getUsername()));

        model.addAttribute("profileMember", memberService.profile(id));
        model.addAttribute("posts", postService.findAllFromMember(id, page, size));

        return "userPage";
    }
}
