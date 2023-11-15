package nora.movlog.domain.movie;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Genre {
    /* 데이터 관리 및 조회용 */
    @Id
    private Integer id;        // DB ID

    /* 장르 데이터 */
    private String name;       // 한국 이름
}
