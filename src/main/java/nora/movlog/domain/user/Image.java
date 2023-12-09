package nora.movlog.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Image {

    /* DB id */
    @Id @GeneratedValue
    private long id;

    /* 이미지 데이터 */
    private String originalFileName;
    private String savedFileName;

    /* 연관관계 */
    @OneToOne(mappedBy = "image")
    private Post post;

    @OneToOne(mappedBy = "image")
    private Member member;
}
