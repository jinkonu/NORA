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
public class Director {
    /* 데이터 관리 및 조회용 */
    @Id
    private String id;      // DB ID

    /* 감독 데이터 */
    private String name;    // 한국 이름

    public static Director create(String id, String name) {
        return new Director(id, name);
    }
}
