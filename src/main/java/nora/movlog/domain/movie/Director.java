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
public class Director {
    /* 데이터 관리 및 조회용 */
    @Id
    private String id;      // DB ID

    /* 감독 데이터 */
    private String name;    // 한국 이름



    public static Set<Director> of(Map<String, String> ids) {
        return ids.entrySet().stream()
                .map(entry -> Director.builder()
                        .id(entry.getKey())
                        .name(entry.getValue())
                        .build())
                .collect(Collectors.toSet());
    }
}
