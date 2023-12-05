package nora.movlog.repository.user;

import nora.movlog.domain.user.Notification;
import nora.movlog.domain.user.Notification.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findAllByFromLoginId(String loginId, Pageable pageable);
    List<Notification> findAllByType(Type type);
    void deleteAllByFromLoginId(String loginId);
}
