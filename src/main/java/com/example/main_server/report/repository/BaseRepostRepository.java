package com.example.main_server.report.repository;

import com.example.main_server.report.entity.BaseReport;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BaseRepostRepository extends MongoRepository<BaseReport, String> {
    List<BaseReport> findAllByUser_UserIdAndType(Long userId, String type);
}
