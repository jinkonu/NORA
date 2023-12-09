package nora.movlog.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.service.user.MemberService;
import nora.movlog.service.user.PostService;
import nora.movlog.utils.MemberFinder;
import org.springframework.security.core.Authentication;
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



    // id 기반 프로필
    @GetMapping(ID_URI)
    public String profilePageFrom(@PathVariable long id,
                                  @RequestParam(defaultValue = DEFAULT_SEARCH_PAGE) int page,
                                  @RequestParam(defaultValue = DEFAULT_SEARCH_SIZE) int size,
                                  Model model,
                                  Authentication auth) {
        model.addAttribute("loginMember", memberService.findByLoginId(MemberFinder.getLoginId(auth)));
        model.addAttribute("profileMember", memberService.profile(id));
        model.addAttribute("posts", postService.findAllFromMemberId(id, page, size));

        return "userPage";
    }


    // 자기 프로필 화면인지 확인
    @ResponseBody
    @GetMapping(ID_URI + "/isSelf")
    public boolean isSelf(@PathVariable long id,
                          Authentication auth) {
        return memberService.profile(id).equals(memberService.findByLoginId(MemberFinder.getLoginId(auth)));
    }


    // 팔로우 신청
    @ResponseBody
    @PostMapping(ID_URI + "/follow")
    public void follow(@PathVariable(name = "id") long followerId,
                       Authentication auth) {
        log.info("FOLLOW!!!");

        String followerLoginId = memberService.profile(followerId).getLoginId();
        String followingLoginId = MemberFinder.getLoginId(auth);

        memberService.follow(followingLoginId, followerLoginId);
    }


    // 팔로우 여부 조회
    @ResponseBody
    @GetMapping(ID_URI + "/isFollowing")
    public boolean isFollowing(@PathVariable(name = "id") long followerId,
                               Authentication auth) {
        String followerLoginId = memberService.profile(followerId).getLoginId();
        String followingLoginId = MemberFinder.getLoginId(auth);

        return memberService.isFollowing(followingLoginId, followerLoginId);
    }


    // 팔로우 취소
    @ResponseBody
    @PostMapping(ID_URI + "/unfollow")
    public void unfollow(@PathVariable(name = "id") long followerId,
                         Authentication auth) {
        log.info("UNFOLLOW!!!");

        String followerLoginId = memberService.profile(followerId).getLoginId();
        String followingLoginId = MemberFinder.getLoginId(auth);

        memberService.unfollow(followingLoginId, followerLoginId);
    }
}
