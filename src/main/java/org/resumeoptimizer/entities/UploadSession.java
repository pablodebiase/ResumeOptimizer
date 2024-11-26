package org.resumeoptimizer.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "upload_sessions")
public class UploadSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long epoch;
    private String resumeFileName;
    private String jobDescFileName;
    private String folderPath;
    private Long userId;
    private String username;
    private String userRole;
    private Double score;
}
