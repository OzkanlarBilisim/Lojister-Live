package com.lojister.business.concretes;

import com.lojister.core.i18n.Translator;
import com.lojister.model.dto.NotificationDto;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.mapper.NotificationMapper;
import com.lojister.model.entity.Notification;
import com.lojister.repository.notification.NotificationRepository;
import com.lojister.business.abstracts.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper notificationMapper;

    private final NotificationRepository notificationRepository;


    @Override
    public NotificationDto getById(Long notificationId) {

        Optional<Notification> notification = notificationRepository.findById(notificationId);

        if (notification.isPresent()) {
            return notificationMapper.entityToDto(notification.get());
        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.notification.EntityNotFoundException"));
        }

    }

    @Override
    public NotificationDto save(NotificationDto notificationDto) {

        Notification notification = notificationMapper.dtoToEntity(notificationDto);

        notification = notificationRepository.save(notification);

        return notificationMapper.entityToDto(notification);
    }

    @Override
    public void deleteById(Long notificationId) {

        Optional<Notification> notification = notificationRepository.findById(notificationId);

        if (notification.isPresent()) {
            notificationRepository.deleteById(notificationId);
        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.notification.EntityNotFoundException"));
        }

    }

    @Override
    public List<NotificationDto> getAll() {
        return notificationMapper.entityListToDtoList(notificationRepository.findAll());
    }
}
