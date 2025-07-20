package com.sk.skala.skore.activityLog.slack.team.entity;

import com.sk.skala.skore.activityLog.slack.chat.entity.SlackFile;
import com.sk.skala.skore.activityLog.slack.chat.entity.SlackMessage;
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
@Table(name = "slack_teams")
public class SlackTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "team_id")
    private String teamId;

    @Column(name = "team_name")
    private String teamName;

    @OneToMany(mappedBy = "slackTeam", fetch = FetchType.LAZY)
    private List<SlackMessage> slackMessages;

    @OneToMany(mappedBy = "slackTeam", fetch = FetchType.LAZY)
    private List<SlackFile> slackFiles;

    @OneToMany(mappedBy = "slackTeam", fetch = FetchType.LAZY)
    private List<SlackMember> slackMembers;
}
