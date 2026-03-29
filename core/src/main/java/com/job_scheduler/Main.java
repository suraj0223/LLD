package com.job_scheduler;

import java.util.UUID;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        JobSchedulerService jobSchedulerService = JobSchedulerService.getInstance();

        // Submit 5 jobs that run concurrently
        for (int i = 1; i <= 5; i++) {
            String jobId = UUID.randomUUID().toString();
            Runnable task = () -> {
                System.out.println("[" + Thread.currentThread().getName() + "] Job Submitted with id ... " + jobId);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            };
            Job job = new Job(jobId, task);
            jobSchedulerService.submitJob(job);
        }

        // Wait for dispatch + execution (6 jobs, pool of 4, needs ~2 rounds)
        Thread.sleep(15000);

        jobSchedulerService.shutdown();
    }
}
