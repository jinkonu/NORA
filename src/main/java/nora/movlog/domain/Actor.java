package nora.movlog.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Actor {
    /* 데이터 관리 및 조회용 */
    @Id
    private String id;      // DB ID

    /* 배우 데이터 */
    private String name;    // 한국 이름
}
