package com.batch.items;

import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import com.batch.model.SimpleCacheObject;
import com.batch.service.NewspaperService;
import com.batch.services.items.ReaderService;

public class NewspaperItemReader implements ItemReader<List<SimpleCacheObject>> {

	ReaderService readerService;
	private boolean isDataNotRead = true;
	int i=0;
	@Autowired
	NewspaperService newspaperService;

	public NewspaperItemReader(ReaderService readerService) {
		this.readerService = readerService;
	}

	@Override
	public List<SimpleCacheObject> read() throws Exception {//by hard coding fixing the issue later re-factor this entire thing.
		List<SimpleCacheObject> rList=null;
		if (isDataNotRead) {
			String currentTimeBatchId = newspaperService.getCurrentTimeBatchId();
			
			System.out.println("************/n isDataNotRead- "+isDataNotRead+" -currentTimeBatchId :"+currentTimeBatchId+" -Loop :"+i+++" \n\n**************");
			
			List<SimpleCacheObject> data = null;
			try {
				long batchId = Long.parseLong(currentTimeBatchId);
				data = readerService.read(batchId);

			} catch (NumberFormatException e) {
				System.out.println("Number Format Exception :: " + currentTimeBatchId);
				return rList;
			}catch (Exception e) {
				return rList;
			}

			isDataNotRead = (data.isEmpty() || data == null) ? true : false;
			System.out.println("isDataNotRead "+isDataNotRead);

			return data;

		} return rList;
	}
}
