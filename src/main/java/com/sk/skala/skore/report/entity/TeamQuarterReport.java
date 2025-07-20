package com.sk.skala.skore.report.entity;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "reports")
public class TeamQuarterReport extends BaseReport {
}
