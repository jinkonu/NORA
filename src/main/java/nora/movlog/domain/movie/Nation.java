package nora.movlog.domain.movie;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Nation {
    /* 데이터 관리 및 조회용 */
    @Id
    private String id;      // DB ID - iso-3166 code


    /* 도메인 로직 */
    public static Set<Nation> of(Set<String> ids) {
        return ids.stream()
                .map(id -> Nation.builder()
                        .id(id)
                        .build())
                .collect(Collectors.toSet());
    }
}
