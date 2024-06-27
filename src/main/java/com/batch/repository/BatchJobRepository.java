package com.batch.repository;

import java.util.Optional;

public interface BatchJobRepository {

	Optional<Long> findCurrentBatchId(int minutesBeforeSearch);
}