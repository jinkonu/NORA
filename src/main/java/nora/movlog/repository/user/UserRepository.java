package nora.movlog.repository.user;

import nora.movlog.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);

    Page<User> findAllByNicknameContains(String nickName, PageRequest pageRequest);

    Boolean existsByLoginId(String loginId);

    Boolean existsByNickname(String nickName);
}
