package nora.movlog.repository.user;

import nora.movlog.domain.user.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginId(String loginId);

    Page<Member> findAllByNicknameContains(String nickName, PageRequest pageRequest);

    Boolean existsByLoginId(String loginId);

    Boolean existsByNickname(String nickName);
}
