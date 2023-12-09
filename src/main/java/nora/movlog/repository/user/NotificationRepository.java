package nora.movlog.repository.user;

import nora.movlog.domain.user.Notification;
import nora.movlog.domain.user.Notification.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findAllByToLoginId(String loginId, Pageable pageable);
    List<Notification> findAllByType(Type type);
    void deleteAllByFromLoginId(String loginId);
}
