package com.job.matchers;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.job.model.Job;
import com.job.model.Worker;

@Component
@Order(value=2)
public class MatchDriverLicense implements Matcher {

	public boolean match(Job job, Worker worker) {
		
		//if license required and worker does not have it return false.
		if(job.isDriverLicenseRequired() && !worker.hasDriversLicense()) {
			return false;
		}
		
		return true;
	}
}
