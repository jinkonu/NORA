package nora.movlog.domain.movie;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Actor {
    /* 데이터 관리 및 조회용 */
    @Id
    private String id;      // DB ID

    /* 배우 데이터 */
    private String name;    // 영어 이름
}
