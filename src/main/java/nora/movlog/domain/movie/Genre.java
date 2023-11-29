package nora.movlog.domain.movie;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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



    /* 도메인 로직 */
    public static Set<Genre> of(Map<Integer, String> ids) {
        return ids.entrySet().stream()
                .map(entry -> Genre.builder()
                        .id(entry.getKey())
                        .name(entry.getValue())
                        .build())
                .collect(Collectors.toSet());
    }
}
