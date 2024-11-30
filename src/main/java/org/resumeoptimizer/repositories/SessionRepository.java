package org.resumeoptimizer.repositories;

import org.resumeoptimizer.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByUsername(String username);
}
