package com.job.model;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Job {

	private static final DateTimeFormatter DATE_TIME_PATTERN = 
			DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	private boolean driverLicenseRequired;
	private List<String> requiredCertificates;
	private Location location;
	private String billRate;
	private int workersRequired;
	private DateTime startDate;
	private String about;
	private String jobTitle;
	private String company;
	private String guid;
	private int jobId;

	public boolean isDriverLicenseRequired() {
		return driverLicenseRequired;
	}
	public void setDriverLicenseRequired(boolean driverLicenseRequired) {
		this.driverLicenseRequired = driverLicenseRequired;
	}
	public List<String> getRequiredCertificates() {
		return requiredCertificates;
	}
	public void setRequiredCertificates(List<String> requiredCertificates) {
		this.requiredCertificates = requiredCertificates;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public double getBillRate() {
		String rate = billRate;
		if(rate == null || rate.isEmpty()) {
			return -1;
		}
		
		if(rate.startsWith("$") && rate.length() > 1) {
			rate = rate.substring(1);
		}
		try {
            return Double.parseDouble(rate);
        } catch (NumberFormatException e) {
            return -1;
        }
	}
	
	public String getBillRateAsString() {
		return billRate;
	}
	public void setBillRate(String billRate) {
		this.billRate = billRate;
	}
	public int getWorkersRequired() {
		return workersRequired;
	}
	public void setWorkersRequired(int workersRequired) {
		this.workersRequired = workersRequired;
	}
	public String getStartDate() {
		//example : "2015-11-12T09:29:19.188Z"
		return startDate.toString(DATE_TIME_PATTERN);
	}
	public void setStartDate(String startDate) {
		this.startDate = DATE_TIME_PATTERN.parseDateTime(startDate);
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public int getJobId() {
		return jobId;
	}
	public void setJobId(int jobId) {
		this.jobId = jobId;
	}
	
	public DateTime getStartDateTime() {
		return startDate;
	}
	
	public class Location {
		private double longitude;
		private double latitude;
		
		public double getLongitude() {
			return longitude;
		}
		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}
		public double getLatitude() {
			return latitude;
		}
		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}
	}

}