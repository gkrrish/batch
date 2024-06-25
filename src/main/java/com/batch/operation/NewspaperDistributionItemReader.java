package com.batch.operation;

import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import com.batch.service.NewspaperDistributionService;

public class NewspaperDistributionItemReader implements ItemReader<List<String>> {

	NewspaperDistributionService newspaperService;

	public NewspaperDistributionItemReader(NewspaperDistributionService newspaperService) {
		this.newspaperService = newspaperService;
	}

	@Override
	public List<String> read()throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		return null;
	}

}
