package com.example.CollegeBackend.repository;

import com.example.CollegeBackend.model.Notice;
import org.springframework.data.repository.CrudRepository;

public interface NoticeRepository extends CrudRepository<Notice, Long> {
}
