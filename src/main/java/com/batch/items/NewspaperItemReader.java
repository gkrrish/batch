package com.batch.items;

import java.util.List;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import com.batch.model.SimpleCacheObject;
import com.batch.service.NewspaperService;
import com.batch.services.items.ReaderService;

public class NewspaperItemReader implements ItemReader<List<SimpleCacheObject>> {

	ReaderService readerService;
	private boolean isDataNotRead = true;
	@Autowired
	NewspaperService newspaperService;

	public NewspaperItemReader(ReaderService readerService) {
		this.readerService = readerService;
	}

	@Override
	public List<SimpleCacheObject> read() throws Exception {
		if (isDataNotRead) {
			String currentTimeBatchId = newspaperService.getCurrentTimeBatchId();
			long batchId = 0;
			List<SimpleCacheObject> data = null;
			try {
				batchId = Long.parseLong(currentTimeBatchId);
				data = readerService.read(batchId);

			} catch (NumberFormatException e) {
				System.out.println("Number Format Exception :: " + currentTimeBatchId);
				return null;
			}

			isDataNotRead = data.isEmpty() || data == null ? true : false;

			return data;

		} else {
			return null;
		}
	}
}
