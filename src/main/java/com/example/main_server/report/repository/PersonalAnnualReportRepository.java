package com.example.main_server.report.repository;

import com.example.main_server.report.entity.PersonalAnnualReport;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonalAnnualReportRepository extends MongoRepository<PersonalAnnualReport, String> {
}
