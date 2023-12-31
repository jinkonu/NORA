package nora.movlog.service.user;

import nora.movlog.service.movie.MovieService;
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

import static nora.movlog.utils.constant.StringConstant.*;
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
    void init() throws IOException {
        memberId = generateMember(
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


    @DisplayName("게시물, 회원과 연관된 좋아요 추가 및 추가 확인")
    @Test
    void add_게시물_회원과_연관된_좋아요_추가_및_추가_확인() {
        likesService.add(TEST_CASE_MEMBER_LOGIN_ID, postId);
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