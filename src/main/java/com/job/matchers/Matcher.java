package com.job.matchers;

import com.job.model.Job;
import com.job.model.Worker;

/**
 * This interface is used to match the worker with the job.
 * @author sunilsonnad
 *
 */
public interface Matcher {

	/**
	 * Returns true if the job is a match for the worker based on the matcher impl.
	 * 
	 * @param job
	 * @param worker
	 * @return
	 */
	public boolean match(Job job, Worker worker);
}
