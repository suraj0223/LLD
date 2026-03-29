package com.job_scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JobStoreTest {

    private JobStore jobStore;

    @BeforeEach
    void setUp() {
        jobStore = new JobStore();
    }

    @Test
    void testSaveAndFindById() {
        Job job = new Job("1", () -> {});
        jobStore.save(job);

        assertTrue(jobStore.findById("1").isPresent());
        assertEquals("1", jobStore.findById("1").get().getJobId());
    }

    @Test
    void testFindByIdNotFound() {
        assertTrue(jobStore.findById("missing").isEmpty());
    }

    @Test
    void testFindByState() {
        Job job1 = new Job("1", () -> {});
        Job job2 = new Job("2", () -> {});
        job2.setState(JobState.RUNNING);

        jobStore.save(job1);
        jobStore.save(job2);

        List<Job> created = jobStore.findByState(JobState.CREATED);
        assertEquals(1, created.size());
        assertEquals("1", created.get(0).getJobId());
    }

    @Test
    void testDelete() {
        Job job = new Job("1", () -> {});
        jobStore.save(job);

        assertTrue(jobStore.delete("1"));
        assertTrue(jobStore.findById("1").isEmpty());
        assertFalse(jobStore.delete("1"));
    }
}
