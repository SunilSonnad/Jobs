package com.job.matchers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.job.model.Job;
import com.job.model.Worker;

@Component
public class MatcherService {

	/*
	 * Ordered matchers. Check individual matcher file for order number.
	 */
	@Autowired
	private List<Matcher> matchers;

	/**
	 * Returns at most the given number of matching jobs.
	 * @param jobs
	 * @param worker
	 * @param noOfJobs
	 * @return
	 */
	public List<Job> matchJobs(List<Job> jobs, Worker worker, int noOfJobs) {
		//list with size one more than required. 
		//required during sorting.
		List<Job> matchingJobs = new ArrayList<>(noOfJobs + 1);
		
		for(Job job : jobs) {
			if(matchJob(job, worker)) {
				//if match add to list.
				addToList(job, matchingJobs, noOfJobs);
			}
		}
		return matchingJobs;
	}

	private boolean matchJob(Job job, Worker worker) {
		//iterate through ordered matchers and try to find a match.
		for(Matcher m : matchers) {
			if(!m.match(job, worker)) {
				//return false even if one match fails.
				return false;
			}
		}
		return true;
	}
	
	/*
	 * insert jobs to list and keep the order sorted.
	 */
	private void addToList(Job job, List<Job> matchingJobs, int noOfJobs) {
		boolean added = false;
		
		// see if this element has to inserted in between.
		for(int i = 0; i < matchingJobs.size(); i++) {
			//add the highest paid in first position.
			if(job.getBillRate() > matchingJobs.get(i).getBillRate()) {
				matchingJobs.add(i, job);
				added = true;
				break;
			}
		}
		
		if(!added) {
			//adds element at the end.
			matchingJobs.add(job);
		}
		
		// maintain only required no of jobs in the list.
		// delete the element that moved to 4th position.
		if(matchingJobs.size() == (noOfJobs + 1)) {
			matchingJobs.remove(noOfJobs);
		}
	}
}
