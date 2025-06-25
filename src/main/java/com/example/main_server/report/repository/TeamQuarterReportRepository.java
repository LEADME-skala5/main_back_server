package com.example.main_server.report.repository;

import com.example.main_server.report.entity.TeamQuarterReport;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamQuarterReportRepository extends MongoRepository<TeamQuarterReport, String> {
}
