package com.batch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.batch.model.UserDetails;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
	
	@Query(
	        value = "SELECT * FROM ( " +
	                "  SELECT ud1_0.*, ROW_NUMBER() OVER (ORDER BY ud1_0.userid) AS row_num " +
	                "  FROM user_details ud1_0 " +
	                ") WHERE row_num BETWEEN :startRow AND :endRow",
	        nativeQuery = true
	    )
	    List<UserDetails> findAllWithStartAndEndRows(int startRow, int endRow);
	
}
