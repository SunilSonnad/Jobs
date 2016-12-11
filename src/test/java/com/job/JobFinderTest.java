package com.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.job.model.Job;
import com.job.model.Worker;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JobFinderTest {

	@Autowired
	private JobFinder jf;
	
	@Autowired
	private RestClient client;
	
	private RestTemplate rest;
	
	ObjectMapper mapper = new ObjectMapper();
	
	@Rule
	public ExpectedException expectedExp = ExpectedException.none();
	
	@Test
	public void testWorkerWith3Jobs() {
		List<Job> jobs = jf.findJobs(5);
		
		assertEquals(3, jobs.size());
		//check order of billrate
		assertTrue(jobs.get(0).getBillRate() > jobs.get(1).getBillRate());
		assertTrue(jobs.get(1).getBillRate() > jobs.get(2).getBillRate());
	}
	
	@Test
	public void testWorkerWith1Job() {
		List<Job> jobs = jf.findJobs(3);
		assertEquals(1, jobs.size());
	}
	
	@Test
	public void testWorkerWith0Jobs() {
		//with no matching job
		List<Job> jobs = jf.findJobs(4);
		assertEquals(0, jobs.size());
	}
	
	
	@Test
	public void testWorkerNotFound() throws JsonParseException, JsonMappingException, IOException {
		expectedExp.expect(RuntimeException.class);
		expectedExp.expectMessage("Worker not found");
		
		jf.findJobs(8);
	}
	
	@Before
	public void mockRestTemplate() throws JsonParseException, JsonMappingException, IOException {
		rest = mock(RestTemplate.class);
		client.setRestTemplate(rest);
		
		List<Worker> workers = 
				mapper.readValue(getClass().getClassLoader().getResource("workers.txt"), 
						new TypeReference<List<Worker>>(){});
		
		List<Job> jobs = 
				mapper.readValue(getClass().getClassLoader().getResource("jobs.txt"), 
						new TypeReference<List<Job>>(){});
		
		ResponseEntity<List<Worker>> wEntity = new ResponseEntity<List<Worker>>(workers, HttpStatus.OK);
		ResponseEntity<List<Job>> jEntity = new ResponseEntity<List<Job>>(jobs, HttpStatus.OK);
		
		jf.setClient(client);
		when(rest.exchange("http://swipejobs.azurewebsites.net/api/workers", HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<Worker>>() {})).thenReturn(wEntity);
		
		when(rest.exchange("http://swipejobs.azurewebsites.net/api/jobs", HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<Job>>() {})).thenReturn(jEntity);
		
	}

}
