package nora.movlog.repository;

import nora.movlog.domain.Notification;
import nora.movlog.domain.Notification.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findAllByFromLoginId(String loginId, Pageable pageable);
    List<Notification> findAllByType(Type type);

}
