package nora.movlog.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.service.user.CommentService;
import nora.movlog.utils.dto.user.CommentCreateRequestDto;
import nora.movlog.utils.dto.user.CommentDto;
import nora.movlog.utils.dto.user.CommentEditDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import static nora.movlog.utils.constant.StringConstant.LAST_URL;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/comment")
@Controller
public class CommentController {

    private final CommentService commentService;



    /* CREATE */
    @PostMapping("/{postId}")
    public String add(@PathVariable long postId,
                      @ModelAttribute CommentCreateRequestDto dto,
                      Authentication auth,
                      HttpServletRequest request) {
        commentService.write(dto, postId, ((UserDetails) auth.getPrincipal()).getUsername());

        return "redirect:" + request.getRequestURI();
    }


    /* READ */
    @GetMapping("/{commentId}")
    public CommentDto read(@PathVariable long commentId,
                           Authentication auth,             // read()는 자바스크립트 상에서 호출될텐데, 이때도 auth에 회원 인증 정보가 들어있는지는 확인해봐야 한다
                           HttpServletRequest request) {
        CommentDto comment = commentService.findOne(commentId);

        if (((UserDetails) auth.getPrincipal()).getUsername().equals(comment.getMemberLoginId())) {
            request.getSession().setAttribute(LAST_URL, request.getRequestURI());

            return comment;
        }

        return null;
    }


    /* UPDATE */
    @PostMapping("/{commentId}/edit")
    public String edit(@PathVariable long commentId,
                       @ModelAttribute CommentEditDto dto,
                       Authentication auth,
                       HttpServletRequest request) {
        commentService.edit(commentId, dto, ((UserDetails) auth.getPrincipal()).getUsername());

        String lastUrl = (String) request.getSession().getAttribute(LAST_URL);
        request.getSession().removeAttribute(LAST_URL);

        return "redirect:" + lastUrl;
    }


    /* DELETE */
    @GetMapping("/{commenId}/delete")
    public String delete(@PathVariable long commentId,
                         Authentication auth,
                         HttpServletRequest request) throws URISyntaxException {
        commentService.delete(commentId, ((UserDetails) auth.getPrincipal()).getUsername());

        return "redirect:" + new URI(request.getHeader("Referer")).getPath();
    }
}
