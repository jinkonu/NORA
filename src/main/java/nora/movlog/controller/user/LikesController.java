package nora.movlog.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.service.user.LikesService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;
import java.net.URISyntaxException;

import static nora.movlog.utils.constant.StringConstant.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(LIKE_URI)
@Controller
public class LikesController {

    private final LikesService likesService;



    /* CREATE */
    @GetMapping("/add/{postId}")
    public String add(@PathVariable long postId,
                      Authentication auth,
                      HttpServletRequest request) throws URISyntaxException {
        likesService.add(((UserDetails) auth.getPrincipal()).getUsername(), postId);

        return "redirect:" + new URI(request.getHeader("Referer")).getPath();
    }


    /* DELETE */
    @GetMapping("/delete/{postId}")
    public String delete(@PathVariable long postId,
                         Authentication auth,
                         HttpServletRequest request) throws URISyntaxException {
        likesService.delete(((UserDetails) auth.getPrincipal()).getUsername(), postId);

        return "redirect:" + new URI(request.getHeader("Referer")).getPath();
    }
 }
