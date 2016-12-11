package com.job;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.job.matchers.MatcherService;
import com.job.model.Job;
import com.job.model.Worker;

@Component
public class JobFinder {

	@Autowired
	private RestClient client;
	
	@Autowired
	private MatcherService matcher;
	
	@Value("${service.noOfJobsToReturn}")
	private int noOfJobs;
	
	/**
	 * Returns appropriate jobs for the worker. The jobs
	 * returned are in the order of bill rates.
	 * @param workerId
	 * @return
	 */
	public List<Job> findJobs(int workerId) {
		
		// Latch to continue when the futures complete.
		CountDownLatch latch = new CountDownLatch(2);
		
		// invokes new thread for each request below.
		Future<List<Job>> jobFuture = client.getJobs(latch);
		Future<List<Worker>> workerFuture = client.getWorkers(latch);
		
		List<Job> jobs;
		List<Worker> workers;
		try {
			// wait for both rest calls to return.
			latch.await(5, TimeUnit.SECONDS);

			jobs = jobFuture.get();
			workers = workerFuture.get();
		} catch (InterruptedException | ExecutionException e) {
			//cancel all futures and throw exception.
			jobFuture.cancel(true);
			workerFuture.cancel(true);
			
			throw new RuntimeException("Internal Server Error ", e);
		}
		
		//fetch the workers first to get the properties of our worker.
		Worker w = getWorker(workerId, workers);
		
		//get top 3 jobs available
		List<Job> matchedJobs = matcher.matchJobs(jobs, w, noOfJobs);
		
		return matchedJobs;
	}
	
	/*
	 * Returns the worker if the userId matches one of the workers.
	 * else return null
	 * @param userId
	 * @return
	 */
	private Worker getWorker(int userId, List<Worker> workers) {
		
		for(Worker w: workers) {
			if(w.getUserId() == userId) {
				// found
				return w;
			}
		}
		
		// no worker with given id.
		throw new RuntimeException("Worker not found", 
				new NoHandlerFoundException(HttpMethod.GET.toString(), null, null));
	}

	public void setClient(RestClient client) {
		this.client = client;
	}
	
}
