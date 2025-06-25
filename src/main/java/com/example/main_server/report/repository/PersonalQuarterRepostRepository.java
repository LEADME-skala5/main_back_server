package com.example.main_server.report.repository;

import com.example.main_server.report.entity.PersonalQuarterReport;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonalQuarterRepostRepository extends MongoRepository<PersonalQuarterReport, String> {
}
