package com.job_scheduler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class JobExecutor {

    private final ExecutorService workerPool;
    private final ConcurrentHashMap<String, ReentrantLock> jobLocks = new ConcurrentHashMap<>();

    public JobExecutor(int poolSize) {
        this.workerPool = Executors.newFixedThreadPool(poolSize);
    }

    public void execute(Job job) {
        ReentrantLock lock = jobLocks.computeIfAbsent(job.getJobId(), k -> new ReentrantLock());

        if (!lock.tryLock()) {
            return; // another thread already picked up this job
        }

        try {
            if (job.getState() != JobState.CREATED) {
                return; // guard against double dispatch
            }

            job.setState(JobState.RUNNING);

            workerPool.submit(() -> {
                try {
                    job.getTask().run();
                    job.setState(JobState.COMPLETED);
                    System.out.println("Processing job id ... " + job.getJobId());
                } catch (Exception e) {
                    job.setFailureReason(e.getMessage());
                    job.setState(JobState.FAILED);
                } finally {
                    jobLocks.remove(job.getJobId());
                }
            });
        } finally {
            lock.unlock();
        }
    }

    /**
        Important step: to close the thread if not required any longer.
     */
    public void shutdown() {
        workerPool.shutdown();
    }
}
