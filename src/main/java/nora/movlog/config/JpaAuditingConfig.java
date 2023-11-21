package nora.movlog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/*
* 엔티티의 생성과 수정 시간 등을 기록하기 위한 config
* Post에서 사용됨
*/

@EnableJpaAuditing
@Configuration
public class JpaAuditingConfig {
}
