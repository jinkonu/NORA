package nora.movlog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
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
    @GeneratedValue
    private Long id;        // DB ID


    /* 장르 데이터 */
    private String name;    // 장르 이름

    public Genre(String name) {
        this.name = name;
    }
}
