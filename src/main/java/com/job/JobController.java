package com.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.job.model.Job;

@RestController
@RequestMapping("/api/workers")
public class JobController {
	
	@Autowired
	private JobFinder finder;
	
	@Autowired
	ApplicationContext ctx;
	
	@RequestMapping(value="{workerid}/newjobs", method=RequestMethod.GET)
	public DeferredResult<List<Job>> jobs(@PathVariable(value="workerid") String workerId) {
		
		DeferredResult<List<Job>> result = new DeferredResult<List<Job>>(8000L); //timeout after 8s
		result.setResult(finder.findJobs(Integer.valueOf(workerId)));
		
		return result;
	}

}
