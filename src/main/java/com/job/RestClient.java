package com.job;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.job.model.Job;
import com.job.model.Worker;

@Service
public class RestClient {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${service.jobUrl}")
	private String jobUrl;
	
	@Value("${service.workerUrl}")
	private String workerUrl;
	
	/**
	 * Returns a Future of list of jobs using RestTemplate with Async.
	 * @param latch
	 * @return
	 */
	@Async
	public Future<List<Job>> getJobs(CountDownLatch latch) {
		
		try {
			ResponseEntity<List<Job>> jobs = 
					restTemplate.exchange(
							jobUrl,
							HttpMethod.GET,
							null,
							new ParameterizedTypeReference<List<Job>>() {});
			
			return new AsyncResult<>(jobs.getBody());
		} catch (RestClientException e) {
			// Keeping error handling to minimum.
			throw new RuntimeException("Could not get server response for jobs ", e);
			
		} finally {
			latch.countDown();
		}
	}
	
	/**
	 * Returns a Future of list of workers using RestTemplate with Async.
	 * @param latch
	 * @return
	 */
	@Async
	public Future<List<Worker>> getWorkers(CountDownLatch latch) {
		try {
			ResponseEntity<List<Worker>> workers = 
					restTemplate.exchange(
							workerUrl,
							HttpMethod.GET,
							null,
							new ParameterizedTypeReference<List<Worker>>() {});
			
			return new AsyncResult<>(workers.getBody());
		} catch (RestClientException e) {
			// Keeping error handling to minimum.
			throw new RuntimeException("Could not get server response for workers ", e);
			
		} finally {
			latch.countDown();
		}
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

}
