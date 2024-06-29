package com.batch.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;;

@Repository
public class BatchJobRepositoryImpl implements BatchJobRepository {

	 @PersistenceContext
	    private EntityManager entityManager;
	 
	     @Override//optimize this query later//if it cause the really a problem then finalize the batch scheduled table and id wise base on time make it. instead of this queries
	    public Optional<Long> findCurrentBatchId(int minutesBeforeSearch) {//may that is striaght forward, but analyze use case of this kind of dynamic querying.
	      /*  String sql = "SELECT BATCH_ID " +
	                 "FROM ( " +
	                 "    SELECT BATCH_ID, " +
	                 "           ROUND((TO_DATE(TO_CHAR(SYSDATE, 'HH:MI AM'), 'HH:MI AM') - TO_DATE(DELIVERY_TIME, 'HH:MI AM')) * 24 * 60) AS time_diff " +
	                 "    FROM MASTER_BATCH_JOBS " +
	                 "    WHERE ROUND((TO_DATE(TO_CHAR(SYSDATE, 'HH:MI AM'), 'HH:MI AM') - TO_DATE(DELIVERY_TIME, 'HH:MI AM')) * 24 * 60) < 0 " + // Future time validation
	                 "          AND ABS(ROUND((TO_DATE(TO_CHAR(SYSDATE, 'HH:MI AM'), 'HH:MI AM') - TO_DATE(DELIVERY_TIME, 'HH:MI AM')) * 24 * 60)) <= :minutes_before_search " +
	                 "    ORDER BY ABS(time_diff) " +
	                 ") " +
	                 "WHERE ROWNUM = 1";

	        Query query = entityManager.createNativeQuery(sql);
	        query.setParameter("minutes_before_search", minutesBeforeSearch);

	        Object result;//mid-night 12:00 giving problem due to date change
			try {
				result = query.getSingleResult();//always takes immediate next delivery time only if we give next next interval time also mintusBeforeSearch
			} catch (NoResultException e) {
				result = null;
			}
	     return result != null ? Optional.of(((BigDecimal) result).longValue()) : Optional.empty();*/
	    	 return Optional.of(42L);
	    }

		
}
