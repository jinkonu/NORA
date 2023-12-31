package nora.movlog.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.service.user.CommentService;
import nora.movlog.utils.MemberFinder;
import nora.movlog.utils.dto.user.CommentCreateRequestDto;
import nora.movlog.utils.dto.user.CommentDto;
import nora.movlog.utils.dto.user.CommentEditDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static nora.movlog.utils.constant.StringConstant.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(COMMENT_URI)
@Controller
public class CommentController {

    private final CommentService commentService;



    /* CREATE */
    @PostMapping("/{postId}")
    public String add(@PathVariable long postId,
                      @ModelAttribute CommentCreateRequestDto dto,
                      Authentication auth) {
        commentService.write(dto, postId, ((UserDetails) auth.getPrincipal()).getUsername());

        return "redirect:" + POST_URI + "/" + postId;
    }


    /* READ */
    @ResponseBody
    @GetMapping("/{commentId}")
    public CommentDto read(@PathVariable long commentId,
                           Authentication auth) {
        CommentDto comment = commentService.findOne(commentId);

        if (MemberFinder.getLoginId(auth).equals(comment.getMemberLoginId()))
            return comment;

        return null;
    }


    /* UPDATE */
    @ResponseBody
    @PostMapping("/{commentId}/edit")
    public void edit(@PathVariable long commentId,
                     @ModelAttribute CommentEditDto dto,
                     Authentication auth) {
        commentService.edit(commentId, dto, MemberFinder.getLoginId(auth));
    }


    /* DELETE */
    @ResponseBody
    @GetMapping("/{commentId}/delete")
    public void delete(@PathVariable long commentId,
                       Authentication auth) {
        commentService.delete(commentId, ((UserDetails) auth.getPrincipal()).getUsername());
    }
}
