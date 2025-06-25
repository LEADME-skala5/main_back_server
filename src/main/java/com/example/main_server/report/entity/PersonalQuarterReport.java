package com.example.main_server.report.entity;

import java.util.List;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "reports")
public class PersonalQuarterReport extends BaseReport {

    private User user;

    private Double finalScore;
    private String compareText;

    private List<TeamGoal> teamGoals;

    private List<String> keyAchievements;

    private List<PeerFeedback> peerFeedback;

    private QuarterlyPerformanceSummary quarterlyPerformanceSummary;

    private List<String> workAttitude;

    private String finalComment;

    public static class User {
        private Long userId;
        private String name;
        private String department;
    }

    public static class TeamGoal {
        private String goalName;
        private String assigned;
        private Integer contributionCount;
        private List<Content> contents;
    }

    public static class Content {
        private String description;
        private List<Reference> reference;
    }

    public static class Reference {
        private String label;
        private String excerpt;
    }

    public static class PeerFeedback {
        private String type;
        private List<String> keywords;
    }

    public static class QuarterlyPerformanceSummary {
        private String summaryText;
    }
}
