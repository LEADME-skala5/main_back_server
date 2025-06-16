package com.example.main_server.report.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "personal_quarter_reports")
public class PersonalQuarterReport {
}
