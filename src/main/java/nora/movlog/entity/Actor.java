package nora.movlog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Actor {
    /* 데이터 관리 및 조회용 */
    @Id
    @GeneratedValue
    private Long id;        // DB ID


    /* 배우 데이터 */
    private String name;    // 한국 이름
}
