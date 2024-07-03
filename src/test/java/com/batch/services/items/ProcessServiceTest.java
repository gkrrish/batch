package com.batch.services.items;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.batch.model.SimpleCacheObject;

@SpringBootTest
public class ProcessServiceTest {

	@Autowired
	ProcessService processService;
	static List<SimpleCacheObject> scoList = new ArrayList<>();

	@BeforeAll
	public static void setUp() {
		SimpleCacheObject sco=new SimpleCacheObject();
		sco.setBatchTime(7L);
		sco.setEmail("gkrrish.11@gmail.com");
		sco.setNewsPaperfileName("file:///C:/Users/Gaganam%20Krishna/Downloads/Eenadu-TS%2029-05.pdf");
		scoList.add(sco);

	}

	@Test
	public void processTest() throws Exception {
		processService.process(scoList);

	}

}
