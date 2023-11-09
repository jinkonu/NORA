package nora.movlog.domain.movie;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Nation {
    /* 데이터 관리 및 조회용 */
    @Id
    private String id;      // DB ID - iso-3166 code
}
