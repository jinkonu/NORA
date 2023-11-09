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
public class Actor {
    /* 데이터 관리 및 조회용 */
    @Id
    private String id;      // DB ID

    /* 배우 데이터 */
    private String name;    // 영어 이름

    public static Actor create(String id, String name) {
        return new Actor(id, name);
    }
}
