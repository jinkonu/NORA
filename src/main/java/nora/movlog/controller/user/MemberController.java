package nora.movlog.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.utils.dto.user.MemberJoinRequestDto;
import nora.movlog.utils.dto.user.MemberLoginRequestDto;
import nora.movlog.service.user.PostService;
import nora.movlog.service.user.MemberService;
import org.springframework.security.core.Authentication;
import nora.movlog.utils.validators.MemberValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static nora.movlog.utils.constant.StringConstant.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(MEMBER_URI)
@Controller
public class MemberController {
    private final MemberService memberService;
    private final PostService postService;



    // 프로필
    @GetMapping("/{id}")
    public String profilePage(@PathVariable long id,
                              @RequestParam(defaultValue = DEFAULT_SEARCH_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_SEARCH_SIZE) int size,
                              Model model) {
        model.addAttribute("member", memberService.profile(id));
        model.addAttribute("posts", postService.findAllFromMember(id, page, size));

        return "userPage";
    }
}
