package nora.movlog.controller;

import lombok.RequiredArgsConstructor;
import nora.movlog.service.user.NotificationService;
import nora.movlog.utils.MemberFinder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static nora.movlog.utils.constant.StringConstant.*;

@RequiredArgsConstructor
@RequestMapping(NOTIFICATION_URI)
@Controller
public class NotificationController {

    private final NotificationService notificationService;



    @GetMapping
    public String readNotifications(@RequestParam(defaultValue = DEFAULT_SEARCH_PAGE) int page,
                                    @RequestParam(defaultValue = DEFAULT_SEARCH_SIZE) int size,
                                    Model model,
                                    Authentication auth) {
        String loginId = MemberFinder.getUsernameFrom(auth);
        model.addAttribute("notifications", notificationService.findAll(loginId, page, size));

        return "notificationPage";
    }
}
