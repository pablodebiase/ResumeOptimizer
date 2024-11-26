package org.resumeoptimizer.repositories;

import org.resumeoptimizer.entities.UploadSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UploadSessionRepository extends JpaRepository<UploadSession, Long> {
    List<UploadSession> findByUsername(String username);
}
