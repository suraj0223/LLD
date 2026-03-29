package com.job_scheduler;

import java.util.List;
import java.util.UUID;

public class JobSchedulerService {

    private static final int WORKER_POOL_SIZE = 4;

    private static JobSchedulerService instance;

    private final JobStore jobStore;
    private final JobExecutor jobExecutor;
    private final JobDispatcher jobDispatcher;

    private JobSchedulerService() {
        this.jobStore = new JobStore(); // instantiate a job store
        this.jobExecutor = new JobExecutor(WORKER_POOL_SIZE); // instantiate with fixed thread pool

        this.jobDispatcher = new JobDispatcher(jobStore, jobExecutor); // create a job dispatcher

        this.jobDispatcher.start(); // start the job dispatcher
    }

    public static synchronized JobSchedulerService getInstance() {
        if (instance == null) {
            instance = new JobSchedulerService();
        }
        return instance;
    }

    public Job submitJob(Job job) {
        jobStore.save(job);
        return job;
    }

    public List<Job> getJobsByState(JobState state) {
        return jobStore.findByState(state);
    }

    public void shutdown() {
        jobDispatcher.stop();
        jobExecutor.shutdown();
    }
}
