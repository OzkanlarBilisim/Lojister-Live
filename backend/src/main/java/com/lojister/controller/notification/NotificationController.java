package com.lojister.controller.notification;

import com.lojister.core.util.annotation.Authenticated;
import com.lojister.model.dto.NotificationDto;
import com.lojister.business.abstracts.NotificationService;
import com.lojister.core.api.ApiPaths;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/notification")
@CrossOrigin
@Authenticated
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<NotificationDto> getById(@PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(notificationService.getById(id));

    }

    @GetMapping()
    public ResponseEntity<List<NotificationDto>> getAll() {

        return ResponseEntity.ok(notificationService.getAll());

    }

    @PostMapping()
    public ResponseEntity<NotificationDto> saveNotification(@RequestBody NotificationDto notificationDto) {

        return ResponseEntity.ok(notificationService.save(notificationDto));

    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable(name = "id") Long notificationId) {

        notificationService.deleteById(notificationId);

    }


}
