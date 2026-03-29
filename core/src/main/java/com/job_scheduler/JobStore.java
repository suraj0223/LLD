package com.job_scheduler;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class JobStore {

    private final ConcurrentHashMap<String, Job> jobs = new ConcurrentHashMap<>();

    public void save(Job job) {
        jobs.put(job.getJobId(), job);
    }

    public Optional<Job> findById(String jobId) {
        return Optional.ofNullable(jobs.get(jobId));
    }

    public List<Job> findByState(JobState state) {
        return jobs.values().stream()
                .filter(job -> job.getState() == state)
                .toList();
    }

    public boolean delete(String jobId) {
        return jobs.remove(jobId) != null;
    }
}
