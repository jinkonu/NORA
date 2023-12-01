package nora.movlog.service.user;

import nora.movlog.domain.Notification;
import nora.movlog.repository.NotificationRepository;
import nora.movlog.service.movie.MovieService;
import nora.movlog.utils.dto.user.CommentCreateRequestDto;
import nora.movlog.utils.dto.user.CommentEditDto;
import nora.movlog.utils.dto.user.MemberJoinRequestDto;
import nora.movlog.utils.dto.user.PostCreateRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.stream.IntStream;

import static nora.movlog.domain.Notification.Type.COMMENT;
import static nora.movlog.utils.constant.StringConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("ALL")
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
class CommentServiceTest {

    private static final String TEST_CASE_QUERY = "파고";
    private static final String TEST_CASE_MOVIE_ID = "275";
    private static final String TEST_CASE_POST_BODY = "재밌어요";
    private static final String TEST_CASE_COMMENT_BODY = "진짜요?";

    private static long postId;

    @Autowired
    CommentService commentService;

    @Autowired
    MemberService memberService;

    @Autowired
    MovieService movieService;

    @Autowired
    PostService postService;

    @Autowired
    NotificationRepository notificationRepository;


    @BeforeEach
    void init() {
        generateMember(
                TEST_CASE_MEMBER_LOGIN_ID,
                TEST_CASE_MEMBER_PASSWORD,
                TEST_CASE_MEMBER_NICKNAME
        );

        postId = generatePost(
                TEST_CASE_POST_BODY,
                TEST_CASE_QUERY,
                TEST_CASE_MOVIE_ID,
                TEST_CASE_MEMBER_LOGIN_ID
        );
    }


    @DisplayName("게시물, 회원과 연관된 댓글 작성 및 게시물과 관련한 댓글 열람")
    @Test
    void writeAndFindAllFromPost_게시물_회원과_연관된_댓글_작성_및_게시물과_관련한_댓글_열람() {
        IntStream.range(0, 10).forEach(i ->
                commentService.write(CommentCreateRequestDto.builder()
                        .body(TEST_CASE_COMMENT_BODY)
                        .build(), postId, TEST_CASE_MEMBER_LOGIN_ID));

        commentService.findAllFromPost(postId, 0, 10).forEach(comment -> {
            assertThat(comment.getBody()).isEqualTo(TEST_CASE_COMMENT_BODY);
            assertThat(postService.findOne(postId).getCommentCnt()).isEqualTo(10);
        });

        assertThat(notificationRepository.findAllByType(COMMENT).size()).isEqualTo(10);
    }


    @DisplayName("댓글 수정")
    @ValueSource(strings = "재미없을듯")
    @ParameterizedTest
    void edit_댓글_수정(String changedBody) {
        long id = commentService.write(CommentCreateRequestDto.builder()
                .body(TEST_CASE_COMMENT_BODY)
                .build(), postId, TEST_CASE_MEMBER_LOGIN_ID)
                .getId();

        commentService.edit(id, CommentEditDto.builder()
                .body(changedBody)
                .build(), TEST_CASE_MEMBER_LOGIN_ID);

        assertThat(commentService.findOne(id).getBody()).isEqualTo(changedBody);
    }


    @DisplayName("댓글 삭제")
    @Test
    void delete_댓글_삭제() {
        long id = commentService.write(CommentCreateRequestDto.builder()
                .body(TEST_CASE_COMMENT_BODY)
                .build(), postId, TEST_CASE_MEMBER_LOGIN_ID)
                .getId();

        commentService.delete(id, TEST_CASE_MEMBER_LOGIN_ID);

        assertThatThrownBy(() ->
                commentService.findOne(id)
        ).isInstanceOf(NoSuchElementException.class);
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


    private long generatePost(String body, String query, String movieId, String memberLoginId) {
        movieService.findAndJoinFromTmdb(query, 0);

        return postService.write(PostCreateRequestDto.builder()
                .body(body)
                .movieId(movieId)
                .build(), memberLoginId);
    }
}