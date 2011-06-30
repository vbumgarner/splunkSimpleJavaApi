package com.splunk.search;

public class JobId {
	private final String jobId;

	public JobId(String jobId) {
		super();
		this.jobId = jobId;
	}

	public String getJobId() {
		return jobId;
	}
	
	@Override
	public String toString() {
		return jobId;
	}
	
	@Override
	public boolean equals(Object obj) {
		return jobId.equals(obj.toString());
	}
	
	@Override
	public int hashCode() {
		return jobId.hashCode();
	}
}
