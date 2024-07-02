package com.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.batch.model.SimpleCacheObject;

@Repository
public interface SimpleCacheObjectRepository extends JpaRepository<SimpleCacheObject, Long> {
}