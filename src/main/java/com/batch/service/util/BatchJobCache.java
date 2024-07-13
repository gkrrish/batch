package com.batch.service.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.batch.model.BatchJob;
import com.batch.repository.BatchJobRepository;

@Service
public class BatchJobCache {
	private static final Logger logger = LoggerFactory.getLogger(BatchJobCache.class);

	@Autowired
	private BatchJobRepository batchJobRepository;

	@Cacheable(value = "batchJobs")
	public List<BatchJob> getBatchJobs() {
		logger.info("Master Batch Job records are fetching from database");
		return batchJobRepository.findAll();
	}
	
	/**
	 * Whenever you are modifying or updating the MASTER_BATCH_JOBS table you should be call this method
	 * @param batchJob
	 * @return
	 */
	@CacheEvict(value = "batchJobs", allEntries = true)
    public BatchJob updateBatchJob(BatchJob batchJob) {
        return batchJobRepository.save(batchJob);
    }

	public Optional<Long> findCurrentBatchId(int minutesBeforeSearch) {
	    LocalTime now = LocalTime.now();
	    List<BatchJob> batchJobs = getBatchJobs();

	    Optional<Long> batchId = batchJobs.stream()
	        .map(job -> {
	            try {
	                LocalTime deliveryTime = LocalTime.parse(job.getDeliveryTime().trim(), DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH));
	                long diff = ChronoUnit.MINUTES.between(now, deliveryTime);
	                if (diff < -720) {
	                    diff += 1440;
	                } else if (diff > 720) {
	                    diff -= 1440;
	                }
	                return (diff >= 0 && diff <= minutesBeforeSearch) ? Map.entry(diff, job.getBatchId()) : null;
	            } catch (DateTimeParseException e) {
	                throw new RuntimeException("Error parsing time for job " + job.getBatchId(), e);
	            }
	        })
	        .filter(Objects::nonNull)
	        .min(Map.Entry.comparingByKey())
	        .map(Map.Entry::getValue);

	    return batchId;
	}

}
