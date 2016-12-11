package com.job.matchers;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.job.model.Job;
import com.job.model.Worker;

@Component
@Order(value=3)
public class MatchLocation implements Matcher {

	private static final String UNIT_KM = "km";

	public boolean match(Job job, Worker worker) {
		
		if(worker.getJobSearchAddress() == null || job.getLocation() == null) {
			return false;
		}

		// find the distance.
		double dist = 
				distance(worker.getJobSearchAddress().getLatitude(),
						worker.getJobSearchAddress().getLongitude(),
						job.getLocation().getLatitude(),
						job.getLocation().getLongitude(),
						(worker.getJobSearchAddress().getUnit() == null) ? UNIT_KM : worker.getJobSearchAddress().getUnit());
		
		// check within proximity.
		if(dist <= worker.getJobSearchAddress().getMaxJobDistance()) {
			return true;
		}
		return false;
	}
	
	/*
	 * Returns the distance between two points in the given unit.
	 * There could be a small difference in the calculation between various algorithms.
	 * @param lat1
	 * @param lon1
	 * @param lat2
	 * @param lon2
	 * @param unit
	 * @return
	 */
	private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (UNIT_KM.equals(unit)) {
			dist = dist * 1.609344;
		}
		
		return (dist);
	}

	//This function converts decimal degrees to radians
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	//This function converts radians to decimal degrees
	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
}
