package com.mzanzibuilds.backend.project.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public class ProjectUpdateResponse {
    public Long id;
    public String title;
    public String content;
    public Integer progressPercent;
    public String projectName;
    public String username;
    public LocalDateTime createdAt;
    public Long projectId; 
}