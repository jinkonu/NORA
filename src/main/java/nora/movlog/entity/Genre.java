package nora.movlog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Genre {
    /* 데이터 관리 및 조회용 */
    @Id @GeneratedValue
    private Long id;    // DB ID



    String name;        // 장르명
}
