package com.example.main_server.common.entity;

import com.example.main_server.slack.chat.entity.SlackFile;
import com.example.main_server.slack.chat.entity.SlackMessage;
import com.example.main_server.slack.team.entity.SlackMember;
import com.example.main_server.teams.chat.entity.TeamsMessage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "slack_user_id")
    private String slackUserId;

    @Column(name = "teams_email")
    private String teamsEmail;

    @Column(name = "teams_user_id")
    private String teamsUserId;

    @Column(name = "onedrive_user_id")
    private String onedriveUserId;

    @Column(name = "name")
    private String name;

    @Column(name = "employee_number")
    private String employeeNumber;

    @Column(name = "password")
    private String password;

    @Column(name = "primary_email")
    private String primaryEmail;

    @Column(name = "slack_email")
    private String slackEmail;

    @Column(name = "local_path")
    private String localPath;

    @Column(name = "department")
    private String department;

    @Column(name = "division")
    private String division;

    @Column(name = "organization")
    private String organization;

    @Column(name = "position")
    private String position;

    @Column(name = "career_level")
    private String careerLevel;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<SlackMessage> slackMessages;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<SlackFile> slackFiles;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<SlackMember> slackMembers;

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    private List<TeamsMessage> teamsMessages;

}
