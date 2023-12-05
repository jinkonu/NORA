package nora.movlog.utils.dto;

import lombok.Builder;
import lombok.Data;
import nora.movlog.domain.user.Notification;

import java.time.format.DateTimeFormatter;

@Builder
@Data
public class NotificationDto {

    private final String toMemberNickname;
    private final String fromMemberNickname;
    private final String type;
    private final String createdAt;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");



    public static NotificationDto of(Notification notification) {
        return NotificationDto.builder()
                .toMemberNickname(notification.getTo().getNickname())
                .fromMemberNickname(notification.getFrom().getNickname())
                .type(notification.getType().getName())
                .createdAt(formatter.format(notification.getCreatedAt()))
                .build();
    }
}
