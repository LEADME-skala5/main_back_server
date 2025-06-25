package com.example.main_server.report.repository;

import com.example.main_server.report.entity.TeamAnnualReport;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamAnnualReportRepository extends MongoRepository<TeamAnnualReport, String> {
}
