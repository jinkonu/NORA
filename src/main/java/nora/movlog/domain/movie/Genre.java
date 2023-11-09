package nora.movlog.domain.movie;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Genre {
    /* 데이터 관리 및 조회용 */
    @Id
    private Integer id;        // DB ID

    /* 장르 데이터 */
    private String name;       // 한국 이름

    public static Genre create(Integer id, String name) {
        return new Genre(id, name);
    }
}
