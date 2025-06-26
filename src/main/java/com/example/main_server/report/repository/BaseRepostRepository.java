package com.example.main_server.report.repository;

import com.example.main_server.report.entity.BaseReport;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseRepostRepository extends MongoRepository<BaseReport, String> {
    List<BaseReport> findAllByUser_UserIdAndTypeIn(Long userId, List<String> types);

    Optional<BaseReport> findById(String documentId);
}
