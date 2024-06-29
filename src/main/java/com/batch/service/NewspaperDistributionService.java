package com.batch.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.batch.repository.BatchJobRepository;

@Service
public class NewspaperDistributionService {

	private final int BEFORE_TIME_IN_MINUTES = 15;

	@Autowired
	BatchJobRepository batchJobRepository;

	public String getCurrentTimeBatchId() {
		Optional<Long> currentBatchId = batchJobRepository.findCurrentBatchId(BEFORE_TIME_IN_MINUTES);//need to validate this query for different scenarios

		if (currentBatchId.isPresent()) {
			return currentBatchId.get().toString();
		} else 
			return "Sorry ! Our radars were not able to be found near the delivery scheduled time.";
		}
}
