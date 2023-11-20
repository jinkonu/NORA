package nora.movlog.service.user;

import nora.movlog.domain.user.Member;
import nora.movlog.dto.user.MemberJoinRequestDto;
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

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
class PostServiceTest {

    private static final String loginId = "hello";

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
}