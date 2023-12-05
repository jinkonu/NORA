package nora.movlog.service.user;

import nora.movlog.domain.user.Member;
import nora.movlog.utils.dto.user.MemberJoinRequestDto;
import nora.movlog.utils.dto.user.PostCreateRequestDto;
import nora.movlog.utils.dto.user.PostDto;
import nora.movlog.service.movie.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static nora.movlog.utils.constant.StringConstant.*;

@SuppressWarnings("ALL")
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
class PostServiceTest {

    private static final String TEST_CASE_MEMBER2_LOGIN_ID = "hello";
    private static final String TEST_CASE_MEMBER2_PASSWORD = "bye";
    private static final String TEST_CASE_MEMBER2_NICKNAME = "beachboys";

    private static final String TEST_CASE_POST_BODY = "hello world!";
    private static final String TEST_CASE_QUERY = "파고";
    private static final String TEST_CASE_MOVIE_ID = "275";

    @Autowired
    PostService postService;
    @Autowired
    MemberService memberService;
    @Autowired
    MovieService movieService;


    @BeforeEach
    void init() {
        generateMember(
                TEST_CASE_MEMBER_LOGIN_ID,
                TEST_CASE_MEMBER_PASSWORD,
                TEST_CASE_MEMBER_NICKNAME);

        movieService.findAndJoinFromTmdb(TEST_CASE_QUERY, 0);
    }


    @DisplayName("게시물 작성 및 조회")
    @Rollback
    @Test
    void write_and_findOne_게시물_작성_및_조회() throws IOException {
        long postId = postService.write(PostCreateRequestDto.builder()
                .body(TEST_CASE_POST_BODY)
                .movieId(TEST_CASE_MOVIE_ID)
                .build(), TEST_CASE_MEMBER_LOGIN_ID);

        assertThat(postService.findOne(postId).getBody()).isEqualTo(TEST_CASE_POST_BODY);
    }


    @DisplayName("회원과 연관된 게시물들 조회")
    @Rollback
    @Test
    void write_and_findAllFromMember() {
        IntStream.range(0, 10)
                .forEach(i -> {
                    try {
                        postService.write(
                                PostCreateRequestDto.builder()
                                        .body(TEST_CASE_POST_BODY)
                                        .movieId(TEST_CASE_MOVIE_ID)
                                        .build(), TEST_CASE_MEMBER_LOGIN_ID);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        List<PostDto> posts = postService.findAllFromMemberLoginId(TEST_CASE_MEMBER_LOGIN_ID, 0, 10);

        assertThat(posts.size()).isEqualTo(10);
        IntStream.range(0, 10)
                .forEach(i -> assertThat(posts.get(i).getBody()).isEqualTo(TEST_CASE_POST_BODY));
    }


    @DisplayName("회원이 팔로우 하는 게시물들 조회")
    @Rollback
    @Test
    void findHomePosts_회원이_팔로우_하는_게시물들_조회() throws IOException {
        generateMember(
                TEST_CASE_MEMBER2_LOGIN_ID,
                TEST_CASE_MEMBER2_PASSWORD,
                TEST_CASE_MEMBER2_NICKNAME);

        Member following = memberService.findByLoginId(TEST_CASE_MEMBER_LOGIN_ID);
        Member follower = memberService.findByLoginId(TEST_CASE_MEMBER2_LOGIN_ID);
        memberService.follow(following.getLoginId(), follower.getLoginId());

        int postSize = 10;
        IntStream.range(0, postSize).forEach(i -> {
            try {
                postService.write(
                        PostCreateRequestDto.builder()
                                .body(TEST_CASE_POST_BODY)
                                .movieId(TEST_CASE_MOVIE_ID)
                                .build(), TEST_CASE_MEMBER2_LOGIN_ID);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        List<PostDto> homePosts = postService.findHomePosts(following.getLoginId());

        assertThat(homePosts.size()).isEqualTo(postSize);
        homePosts.stream()
                .forEach(post -> assertThat(post.getBody()).contains(TEST_CASE_POST_BODY));
    }


    @DisplayName("게시물 수정")
    @ValueSource(strings = "bye world")
    @ParameterizedTest
    void edit_게시물_수정(String changedBody) throws IOException {
        long postId = postService.write(PostCreateRequestDto.builder()
                .body(TEST_CASE_POST_BODY)
                .movieId(TEST_CASE_MOVIE_ID)
                .build(), TEST_CASE_MEMBER_LOGIN_ID);

        long editedPostId = postService.edit(postId, PostDto.builder().body(changedBody).build());

        assertThat(postId).isEqualTo(editedPostId);
        assertThat(postService.findOne(editedPostId).getBody()).isEqualTo(changedBody);
    }


    @DisplayName("게시물 삭제")
    @Test
    void delete_게시물_삭제() throws IOException {
        long postId = postService.write(PostCreateRequestDto.builder()
                .body(TEST_CASE_POST_BODY)
                .movieId(TEST_CASE_MOVIE_ID)
                .build(), TEST_CASE_MEMBER_LOGIN_ID);

        postService.delete(postId);
        assertThat(postService.findOne(postId)).isNull();
    }


    private void generateMember(String loginId, String password, String nickname) {
        MemberJoinRequestDto dto = new MemberJoinRequestDto();
        dto.setLoginId(loginId);
        dto.setPassword(password);
        dto.setPasswordCheck(password);
        dto.setNickname(nickname);

        memberService.join(dto);
    }
}