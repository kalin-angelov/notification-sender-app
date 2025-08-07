package app.repository;

import app.model.EmailNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmailNotificationRepository extends JpaRepository<EmailNotification, UUID> {

    @Query("""
            SELECT n FROM EmailNotification n WHERE n.userId = :userId AND n.deleted = false
            """)
    List<EmailNotification> findAllByUserIdAndDeleteIsFalse(@Param("userId") UUID userId);
}
