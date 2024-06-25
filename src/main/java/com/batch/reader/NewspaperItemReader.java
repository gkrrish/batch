package com.batch.reader;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import com.batch.model.RedisCacheObject;
import com.batch.service.NewspaperService;

public class NewspaperItemReader implements ItemReadListener<List<RedisCacheObject>>, ItemReader<List<String>> {
	private static final Logger log = LoggerFactory.getLogger(NewspaperItemReader.class);

	public NewspaperItemReader(NewspaperService newspaperService) {
		beforeRead();
		afterRead(null);
		onReadError(null);
	}

	@Override
	public void beforeRead() {

		log.info(":: Started Reading ");
	}

	@Override
	public void afterRead(List<RedisCacheObject> item) {
		log.info(":: after Reading ");
	}

	@Override
	public void onReadError(Exception ex) {
		log.info(":: onError Reading ");
	}

	@Override
	public List<String> read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		// TODO Auto-generated method stub
		return null;
	}

}
