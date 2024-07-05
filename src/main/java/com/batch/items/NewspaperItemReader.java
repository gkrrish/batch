package com.batch.items;

import java.util.List;

import org.springframework.batch.item.ItemReader;

import com.batch.model.SimpleCacheObject;
import com.batch.services.items.ReaderService;

public class NewspaperItemReader implements ItemReader<List<SimpleCacheObject>> {

	ReaderService readerService;
	private boolean readComplete = false;

	public NewspaperItemReader(ReaderService readerService) {
		this.readerService = readerService;
	}

	@Override
	public List<SimpleCacheObject> read() throws Exception {
		if (!readComplete) {
			List<SimpleCacheObject> data = readerService.read();
			readComplete = (data == null || data.isEmpty());
			
			if (readComplete) {
				System.out.println("remove this later if condition :: Read operation completed or no data found.");
			} else {
				System.out.println("remove this later if condition :: Data read successfully: " + data);
				reset();//or we can manage through listeners
			}
			
			return data;
		} else {
			reset();
			return null; 
		}
	}

	public void reset() {
		System.out.println("Reset :: on Reader if data is null then it is going to be trigger");
		this.readComplete = false; 
	}

}
