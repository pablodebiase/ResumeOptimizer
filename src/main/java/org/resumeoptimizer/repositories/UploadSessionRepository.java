package org.resumeoptimizer.repositories;

import org.resumeoptimizer.entities.UploadSession;
import org.resumeoptimizer.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UploadSessionRepository extends JpaRepository<UploadSession, Long> {
    List<UploadSession> findByUser(User user);
}
