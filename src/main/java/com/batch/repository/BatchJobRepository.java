package com.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.batch.model.BatchJob;

@Repository
public interface BatchJobRepository extends JpaRepository<BatchJob, Long> {
}