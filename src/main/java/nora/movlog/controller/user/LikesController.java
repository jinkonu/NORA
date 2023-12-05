package nora.movlog.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.service.user.LikesService;
import nora.movlog.service.user.PostService;
import nora.movlog.utils.MemberFinder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static nora.movlog.utils.constant.StringConstant.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(LIKE_URI)
@RestController
public class LikesController {

    private final LikesService likesService;


    /* CREATE */
    @PostMapping("/{postId}/add")
    public void add(@PathVariable long postId,
                    Authentication auth) {
        likesService.add(MemberFinder.getLoginId(auth), postId);
    }


    /* READ */
    @GetMapping("/{postId/check")
    public boolean check(@PathVariable long postId,
                         Authentication auth) {
        return likesService.check(MemberFinder.getLoginId(auth), postId);
    }


    /* DELETE */
    @PostMapping("/{postId}/delete")
    public void delete(@PathVariable long postId,
                       Authentication auth) {
        likesService.delete(MemberFinder.getLoginId(auth), postId);
    }
 }
