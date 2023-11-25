package nora.movlog.service.user;

import nora.movlog.service.movie.MovieService;
import nora.movlog.utils.dto.user.MemberJoinRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

import static nora.movlog.domain.constant.StringConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ALL")
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
class LikesServiceTest {

    private static final String TEST_CASE_QUERY = "파고";
    private static final String TEST_CASE_MOVIE_ID = "275";
    private static final String TEST_CASE_POST_BODY = "재밌어요";

    private static long memberId;
    private static long postId;

    @Autowired
    LikesService likesService;

    @Autowired
    MemberService memberService;

    @Autowired
    MovieService movieService;

    @Autowired
    PostService postService;

    @BeforeEach
    void init() {
        memberId = generateMember(
                TEST_CASE_MEMBER_LOGIN_ID,
                TEST_CASE_MEMBER_PASSWORD,
                TEST_CASE_MEMBER_NICKNAME
        );

        postId = generatePost(
                TEST_CASE_POST_BODY,
                TEST_CASE_QUERY,
                TEST_CASE_MOVIE_ID,
                memberId
        );
    }


    @DisplayName("게시물, 회원과 연관된 좋아요 추가 및 추가 확인")
    @Test
    void add_게시물_회원과_연관된_좋아요_추가_및_추가_확인() {
        likesService.add(memberId, postId);

        assertThat(likesService.check(memberId, postId)).isTrue();
    }


    @DisplayName("게시물, 회원과 연관된 좋아요 삭제 및 삭제 확인")
    @Test
    void delete_게시물_회원과_연관된_좋아요_삭제_및_삭제_확인() {
        likesService.add(memberId, postId);

        likesService.delete(memberId, postId);
        assertThat(likesService.check(memberId, postId)).isFalse();
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


    private long generatePost(String body, String query, String movieId, long memberId) {
        movieService.findAndJoinFromTmdb(query, 0);

        return postService.write(body, movieId, memberId);
    }
}