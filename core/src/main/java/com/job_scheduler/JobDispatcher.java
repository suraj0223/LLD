package com.job_scheduler;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JobDispatcher {

    private static final long POLL_INTERVAL_MS = 300;

    private final JobStore jobStore;
    private final JobExecutor jobExecutor;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public JobDispatcher(JobStore jobStore, JobExecutor executor) {
        this.jobStore = jobStore;
        this.jobExecutor = executor;
    }

    public void start() {
        scheduler.scheduleAtFixedRate(this::dispatchCycle, 0, POLL_INTERVAL_MS, TimeUnit.MILLISECONDS);
    }

    private void dispatchCycle() {
        List<Job> eligibleJobs = jobStore.findByState(JobState.CREATED);
        for (Job job : eligibleJobs) {
            jobExecutor.execute(job);
        }
    }

    public void stop() {
        scheduler.shutdown();
    }
}
