package com.batch.service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.batch.model.BatchJob;
import com.batch.repository.BatchJobRepository;

@Service
public class MasterBatchJobRetrivingCacheService {

	@Autowired
	private BatchJobRepository batchJobRepository;

//    @Cacheable("batchJobs")
	public List<BatchJob> getBatchJobs() {
		return batchJobRepository.findAll();
	}

	public Optional<Long> findCurrentBatchId(int minutesBeforeSearch) {//need to validate this query for different scenarios , check times very precisely.
	    LocalTime now = LocalTime.now();
	    List<BatchJob> batchJobs = getBatchJobs();
	    
	    Optional<Long> batchId = batchJobs.stream()
				.map(job -> {

					try {
						LocalTime time = LocalTime.parse(job.getDeliveryTime().trim(),DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH));

						long diff = ChronoUnit.MINUTES.between(time, now);
						return (diff < 0 && -diff <= minutesBeforeSearch) ? Map.entry(-diff, job.getBatchId()) : null;

					} catch (DateTimeParseException e) {
						throw new RuntimeException("Error parsing time for job " + job.getBatchId(), e);
					}
				})
	        
	        .filter(Objects::nonNull) 
	        .min(Map.Entry.comparingByKey())
	        .map(Map.Entry::getValue);
	    
	    return batchId;
	}

	@Transactional
	public void updateBatchJobAndClearsCache(BatchJob batchJob) {
		batchJobRepository.save(batchJob);
		evictCacheClearsCacheEmptyMethod();
	}
	@CacheEvict(value = "batchJobs", allEntries = true)
	public void evictCacheClearsCacheEmptyMethod() {
	}
}
