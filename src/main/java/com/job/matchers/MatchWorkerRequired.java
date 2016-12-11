package com.job.matchers;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.job.model.Job;
import com.job.model.Worker;

@Component
@Order(value=1)
public class MatchWorkerRequired implements Matcher {

	public boolean match(Job job, Worker worker) {
		
		if(job.getWorkersRequired() > 0) {
			return true;
		}
		
		return false;
	}
}
