package com.example.main_server.report.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "team_annual_reports")
public class TeamAnnualReport {
}
