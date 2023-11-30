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
public class Actor {
    /* 데이터 관리 및 조회용 */
    @Id
    private String id;      // DB ID

    /* 배우 데이터 */
    private String name;    // 영어 이름



    /* 도메인 로직 */
    public static Set<Actor> of(Map<String, String> ids) {
        return ids.entrySet().stream()
                .map(entry -> Actor.builder()
                        .id(entry.getKey())
                        .name(entry.getValue())
                        .build())
                .collect(Collectors.toSet());
    }
}
