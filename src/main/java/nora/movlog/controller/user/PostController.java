package nora.movlog.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.service.user.PostService;
import nora.movlog.utils.dto.user.PostCreateRequestDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static nora.movlog.utils.constant.StringConstant.LAST_URL;
import static nora.movlog.utils.constant.StringConstant.POST_URI;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(POST_URI)
@Controller
public class PostController {

    private final PostService postService;



    @GetMapping
    public String postPage(HttpServletRequest request) {
        request.getSession().setAttribute(LAST_URL, request.getRequestURI());

        return "writePost";
    }

    @PostMapping
    public String writePost(@ModelAttribute PostCreateRequestDto dto,
                            Authentication auth,
                            HttpServletRequest request) {
        postService.write(dto, ((UserDetails) auth.getPrincipal()).getUsername());

        String lastUrl = (String) request.getSession().getAttribute(LAST_URL);
        request.getSession().removeAttribute(LAST_URL);

        return "redirect:" + lastUrl;
    }
}
