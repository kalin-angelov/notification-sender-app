package app.repository;

import app.model.EmailNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailNotificationRepository extends JpaRepository<EmailNotification, UUID> {

    Optional<List<EmailNotification>> findAllByUserIdAndIsDeleted(UUID userId, boolean isDeleted);
}
