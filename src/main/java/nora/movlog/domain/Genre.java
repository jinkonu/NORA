package nora.movlog.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
public class
Genre {
    /* 데이터 관리 및 조회용 */
    @Id
    private String id;        // DB ID


    /* 장르 데이터 */
    private String name;      // 한국 이름

    public Genre(String name) {
        this.name = name;
    }
}
