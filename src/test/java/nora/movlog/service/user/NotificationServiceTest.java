package nora.movlog.service.user;

import nora.movlog.service.movie.MovieService;
import nora.movlog.service.user.CommentService;
import nora.movlog.service.user.MemberService;
import nora.movlog.service.user.NotificationService;
import nora.movlog.service.user.PostService;
import nora.movlog.utils.dto.NotificationDto;
import nora.movlog.utils.dto.user.CommentCreateRequestDto;
import nora.movlog.utils.dto.user.MemberJoinRequestDto;
import nora.movlog.utils.dto.user.PostCreateRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static nora.movlog.utils.constant.StringConstant.*;
import static nora.movlog.utils.constant.StringConstant.TEST_CASE_MEMBER_LOGIN_ID;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ALL")
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
class NotificationServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    CommentService commentService;
    @Autowired
    MovieService movieService;
    @Autowired
    PostService postService;
    @Autowired
    NotificationService notificationService;

    private static final String TEST_CASE_MEMBER2_LOGIN_ID = "hello";
    private static final String TEST_CASE_MEMBER2_PASSWORD = "bye";
    private static final String TEST_CASE_MEMBER2_NICKNAME = "beachboys";

    private static final String TEST_CASE_QUERY = "파고";
    private static final String TEST_CASE_MOVIE_ID = "275";
    private static final String TEST_CASE_POST_BODY = "재밌어요";
    private static final String TEST_CASE_COMMENT_BODY = "진짜요?";

    private static long postId;



    @BeforeEach
    void init() throws IOException {
        generateMember(
                TEST_CASE_MEMBER_LOGIN_ID,
                TEST_CASE_MEMBER_PASSWORD,
                TEST_CASE_MEMBER_NICKNAME
        );

        generateMember(
                TEST_CASE_MEMBER2_LOGIN_ID,
                TEST_CASE_MEMBER2_PASSWORD,
                TEST_CASE_MEMBER2_NICKNAME
        );

        postId = generatePost(
                TEST_CASE_POST_BODY,
                TEST_CASE_QUERY,
                TEST_CASE_MOVIE_ID,
                TEST_CASE_MEMBER_LOGIN_ID
        );

        // 댓글 작성
        IntStream.range(0, 5).forEach(i ->
                commentService.write(CommentCreateRequestDto.builder()
                        .body(TEST_CASE_COMMENT_BODY)
                        .build(), postId, TEST_CASE_MEMBER_LOGIN_ID));

        // 팔로우
        memberService.follow(TEST_CASE_MEMBER_LOGIN_ID, TEST_CASE_MEMBER2_LOGIN_ID);
    }


    @DisplayName("회원과 관련한 알림 조회")
    @Test
    void findAll_회원과_관련한_알림_조회() {
        List<NotificationDto> notifications1 = notificationService.findAll(TEST_CASE_MEMBER_LOGIN_ID, 0, 10);

        notifications1.stream()
                .forEach(noti -> System.out.println(noti.getType()));

        List<NotificationDto> comments = notifications1.stream()
                .filter(noti -> noti.getType().equals(COMMENT_TYPE))
                .collect(Collectors.toList());

        List<NotificationDto> follows = notifications1.stream()
                .filter(noti -> noti.getType().equals(FOLLOW_TYPE))
                .collect(Collectors.toList());

        assertThat(comments.size()).isEqualTo(5);
        assertThat(follows.size()).isEqualTo(1);
    }


    private long generateMember(String loginId, String password, String nickname) {
        MemberJoinRequestDto dto = new MemberJoinRequestDto();
        dto.setLoginId(loginId);
        dto.setPassword(password);
        dto.setPasswordCheck(password);
        dto.setNickname(nickname);

        memberService.join(dto);

        return memberService.findByLoginId(loginId).getId();
    }


    private long generatePost(String body, String query, String movieId, String memberLoginId) throws IOException {
        movieService.findAndJoinFromTmdb(query, 0);

        return postService.write(PostCreateRequestDto.builder()
                .body(body)
                .movieId(movieId)
                .build(), memberLoginId);
    }
}