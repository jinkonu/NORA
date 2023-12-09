package nora.movlog.service.user;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.user.Notification;
import nora.movlog.domain.user.Comment;
import nora.movlog.domain.user.Likes;
import nora.movlog.domain.user.Member;
import nora.movlog.repository.user.NotificationRepository;
import nora.movlog.utils.dto.NotificationDto;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static nora.movlog.domain.user.Notification.Type.*;
import static nora.movlog.utils.constant.StringConstant.FOLLOWER;
import static nora.movlog.utils.constant.StringConstant.FOLLOWING;


@RequiredArgsConstructor
@Transactional
@Aspect
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public static final Sort sort = Sort.by(Sort.Order.desc("lastModifiedAt"));



    /* CREATE */
    @AfterReturning(
            pointcut = "execution( * nora.movlog.service.user.LikesService.add(..))",
            returning = "likes"
    )
    public void joinLikesFrom(Likes likes) {
        notificationRepository.save(Notification.builder()
                .to(likes.getPost().getMember())
                .from(likes.getMember())
                .type(LIKE)
                .build());
    }


    @AfterReturning(
            pointcut = "execution( * nora.movlog.service.user.CommentService.write(..))",
            returning = "comment"
    )
    public void joinCommentFrom(Comment comment) {
        notificationRepository.save(Notification.builder()
                .to(comment.getPost().getMember())
                .from(comment.getMember())
                .type(COMMENT)
                .build());
    }


    @AfterReturning(
            pointcut = "execution( * nora.movlog.service.user.MemberService.follow(..))",
            returning = "members"
    )
    public void joinFollowFrom(Map<String, Member> members) {
        notificationRepository.save(Notification.builder()
                .to(members.get(FOLLOWER))
                .from(members.get(FOLLOWING))
                .type(FOLLOW)
                .build());
    }


    @AfterReturning(
            pointcut = "execution( * nora.movlog.service.user.MemberService.unfollow(..))",
            returning = "members"
    )
    public void joinUnfollowFrom(Map<String, Member> members) {
        notificationRepository.save(Notification.builder()
                .to(members.get(FOLLOWER))
                .from(members.get(FOLLOWING))
                .type(UNFOLLOW)
                .build());
    }


    /* READ */
    public List<NotificationDto> findAll(String loginId, int page, int size) {
        return notificationRepository.findAllByToLoginId(loginId, PageRequest.of(page, size, sort))
                .stream()
                .sorted(Notification::compareTo)
                .map(NotificationDto::of)
                .toList();
    }


    /* DELETE */
    public void deleteAll(String loginId) {
        notificationRepository.deleteAllByFromLoginId(loginId);
    }
}
