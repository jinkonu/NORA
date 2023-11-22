package nora.movlog.service.user;

import nora.movlog.domain.user.Member;
import nora.movlog.utils.dto.user.MemberJoinRequestDto;
import nora.movlog.utils.dto.user.PostDto;
import nora.movlog.utils.dto.user.PostEditDto;
import nora.movlog.service.movie.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ALL")
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
class PostServiceTest {

    private static final String loginId = "hello2";

    @Autowired
    PostService postService;
    @Autowired
    MemberService memberService;
    @Autowired
    MovieService movieService;


    @BeforeEach
    void init() {
        MemberJoinRequestDto dto = new MemberJoinRequestDto();
        dto.setLoginId(loginId);
        dto.setPassword("bye");
        dto.setPasswordCheck("bye");
        dto.setNickname("beatles");

        memberService.join(dto);
        movieService.findAndJoinFromTmdb("파고", 0);
    }


    @DisplayName("게시물 작성 및 조회")
    @Rollback
    @ValueSource(strings = "hello world!")
    @ParameterizedTest
    void write_and_findOne_게시물_작성_및_조회(String body) {
        Member member = memberService.findByLoginId(loginId);
        long postId = postService.write(body, "275", member.getId());

        assertThat(postService.findOne(postId).getBody()).isEqualTo(body);
    }


    @DisplayName("회원과 연관된 게시물들 조회")
    @Rollback
    @ValueSource(strings = "hello world!")
    @ParameterizedTest
    void write_and_findAllFromOneUser(String body) {
        Member member = memberService.findByLoginId(loginId);

        IntStream.range(0, 10)
                .forEach(i -> postService.write(body, "275", member.getId()));

        List<PostDto> posts = postService.findAllFromMember(member.getId(), 0, 10);

        assertThat(posts.size()).isEqualTo(10);
        IntStream.range(0, 10)
                .forEach(i -> assertThat(posts.get(i).getBody()).isEqualTo(body));
    }


    @DisplayName("게시물 수정")
    @ValueSource(strings = "hello world,bye world")
    @ParameterizedTest
    void edit_게시물_수정(String input) {
        String[] bodies = input.split(",");

        Member member = memberService.findByLoginId(loginId);
        long postId = postService.write(bodies[0], "275", member.getId());
        long editedPostId = postService.edit(postId, PostEditDto.builder().body(bodies[1]).build());

        assertThat(postId).isEqualTo(editedPostId);
        assertThat(postService.findOne(editedPostId).getBody()).isEqualTo(bodies[1]);
    }


    @DisplayName("게시물 삭제")
    @ValueSource(strings = "hello world!")
    @ParameterizedTest
    void delete_게시물_삭제(String body) {
        Member member = memberService.findByLoginId(loginId);
        long postId = postService.write(body, "275", member.getId());

        postService.delete(postId);
        assertThat(postService.findOne(postId)).isNull();
    }
}