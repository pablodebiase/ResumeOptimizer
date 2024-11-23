package org.resumeoptimizer.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
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

    private String resumeFileName;
    private String jobDescFileName;
    private String folderPath;
    private Double score;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //
    // Other relevant information fields

    // Getters and Setters
}
