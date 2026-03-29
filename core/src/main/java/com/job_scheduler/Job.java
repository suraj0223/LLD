package com.job_scheduler;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Job {

    private final String jobId;
    private Runnable task;
    private JobState state;
    private String failureReason;

    public Job(String jobId, Runnable task) {
        this.jobId = jobId;
        this.task = task;
        this.state = JobState.CREATED;
    }
}
