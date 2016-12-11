package com.job.matchers;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.job.model.Address;
import com.job.model.Job;
import com.job.model.Job.Location;
import com.job.model.Worker;

public class MatcherTest {

	Job job;
	Worker wkr;
	
	@Before
	public void setup() {
		createJob();
		createWorker();
	}

	private void createWorker() {
		wkr = new Worker();
		wkr.setHasDriversLicense(true);
		Address a = new Address();
		a.setLatitude(14.84);
		a.setLongitude(49);
		a.setMaxJobDistance(50);
		a.setUnit("km");
		wkr.setJobSearchAddress(a);
		
		List<String> rc = new ArrayList<>();
		rc.add("The Risk Taker");
		rc.add("Excellence in Organization");
		rc.add("Excellence in Humor");
		wkr.setCertificates(rc);
	}

	private void createJob() {
		job = new Job();
		job.setBillRate("$12.4");
		job.setDriverLicenseRequired(true);
		job.setJobId(1);
		Location l = job.new Location();
		l.setLatitude(14.93);
		l.setLongitude(49.40);
		job.setLocation(l);
		job.setWorkersRequired(3);
		
		List<String> rc = new ArrayList<>();
		rc.add("Excellence in Humor");
		rc.add("Excellence in Organization");		
		job.setRequiredCertificates(rc);
	}
	
	@Test
	public void testWorkerRequired() {
		Matcher m = new MatchWorkerRequired();
		assertTrue(m.match(job, wkr));
		
		job.setWorkersRequired(0);
		assertFalse(m.match(job, wkr));
	}
	
	@Test
	public void testDriverLicense() {
		Matcher m = new MatchDriverLicense();
		assertTrue(m.match(job, wkr));
		
		wkr.setHasDriversLicense(false);
		assertFalse(m.match(job, wkr));
		
		job.setDriverLicenseRequired(false);
		assertTrue(m.match(job, wkr));
	}
	
	@Test
	public void testLocation() {
		Matcher m = new MatchLocation();
		assertTrue(m.match(job, wkr));
		
		wkr.getJobSearchAddress().setMaxJobDistance(20);
		assertFalse(m.match(job, wkr));
	}

	@Test
	public void testCertificates() {
		Matcher m = new MatchCertificates();
		assertTrue(m.match(job, wkr));
		
		job.getRequiredCertificates().add("Bungy Jumper");
		assertFalse(m.match(job, wkr));
	}
}
