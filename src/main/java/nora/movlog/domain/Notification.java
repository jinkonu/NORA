package nora.movlog.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nora.movlog.domain.user.BaseEntity;
import nora.movlog.domain.user.Member;

import static nora.movlog.utils.constant.NumberConstant.BIGGER;
import static nora.movlog.utils.constant.StringConstant.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Notification extends BaseEntity implements Comparable<Notification> {

    /* DB id */
    @Id @GeneratedValue
    private long id;


    /* 연관관계 */
    @ManyToOne(fetch = FetchType.LAZY)
    private Member to;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member from;

    private Type type;


    /* 메서드 */
    public enum Type {
        COMMENT(COMMENT_TYPE),
        LIKE(LIKES_TYPE),
        FOLLOW(FOLLOW_TYPE);

        private final String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }


    @Override
    public int compareTo(Notification other) {
        if (other == null)
            return BIGGER;

        return this.getCreatedAt().compareTo(other.getCreatedAt());
    }
}
