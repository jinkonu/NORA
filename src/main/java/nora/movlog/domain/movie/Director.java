package nora.movlog.domain.movie;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Director {
    /* 데이터 관리 및 조회용 */
    @Id
    private String id;      // DB ID

    /* 감독 데이터 */
    private String name;    // 한국 이름
}
