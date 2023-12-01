package nora.movlog.utils;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.Notification;
import nora.movlog.domain.user.Comment;
import nora.movlog.domain.user.Likes;
import nora.movlog.domain.user.Member;
import nora.movlog.repository.NotificationRepository;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Map;

import static nora.movlog.domain.Notification.Type.*;
import static nora.movlog.utils.constant.StringConstant.FOLLOWER;
import static nora.movlog.utils.constant.StringConstant.FOLLOWING;


@RequiredArgsConstructor
@Aspect
@Component
public class NotificationAspect {

    private final NotificationRepository notificationRepository;


    @AfterReturning(
            pointcut = "execution( * nora.movlog.service.user.LikesService.add(..))",
            returning = "likes"
    )
    public void generateNotificationFrom(Likes likes) {
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
    public void generateNotificationFrom(Comment comment) {
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
    public void generateNotificationFrom(Map<String, Member> members) {
        notificationRepository.save(Notification.builder()
                .to(members.get(FOLLOWER))
                .from(members.get(FOLLOWING))
                .type(FOLLOW)
                .build());
    }
}
