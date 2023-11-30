package nora.movlog.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nora.movlog.domain.user.Member;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Notification {

    /* DB id */
    @Id @GeneratedValue
    private long id;


    /* 연관관계 */
    @ManyToOne(fetch = FetchType.LAZY)
    private Member to;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member from;

    private enum Type { COMMENT, LIKE }
}
