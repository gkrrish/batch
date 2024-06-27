package com.batch.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.batch.repository.BatchJobRepository;

@Service
public class NewspaperDistributionService {
	
	private final int BEFORE_TIME_IN_MINUTES = 5;

	@Autowired
	BatchJobRepository batchJobRepository;

	public String getCurrentTimeBatchId() {
		Optional<Long> currentBatchId = batchJobRepository.findCurrentBatchId(BEFORE_TIME_IN_MINUTES);
		String output = currentBatchId.isPresent() ? "Batch ID: " + currentBatchId.get().toString() : "Sorry, we did not meet the time.";
		return output;
	}

}
