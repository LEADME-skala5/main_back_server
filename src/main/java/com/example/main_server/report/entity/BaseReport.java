package com.example.main_server.report.entity;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reports")
public abstract class BaseReport {
    @Id
    private String id;

    private String type;
    private Integer evaluatedYear;
    private Integer evaluatedQuarter;
    private String createdAt;
    private String title;
    private String startDate;
    private String endDate;

    private User user;

    public static class User {
        private Long userId;
        private String name;
        private String department;
    }
}
