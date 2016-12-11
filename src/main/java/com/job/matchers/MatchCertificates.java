package com.job.matchers;

import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.job.model.Job;
import com.job.model.Worker;

@Component
@Order(value=4)
public class MatchCertificates implements Matcher {

	public boolean match(Job job, Worker worker) {

		List<String> certs = worker.getCertificates();
		// check if all required certificates are met by worker.
		if(certs.containsAll(job.getRequiredCertificates())) {
			return true;
		}
		
		return false;
	}
}
