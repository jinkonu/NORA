package nora.movlog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Director {
    /* 데이터 관리 및 조회용 */
    @Id @GeneratedValue
    private Long id;        // for database


    /* 감독 데이터 */
    private String name;    // for korean name
}
