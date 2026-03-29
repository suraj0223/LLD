package com.job_scheduler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class JobExecutorTest {

    private JobExecutor executor;

    @BeforeEach
    void setUp() {
        executor = new JobExecutor(4);
    }

    @AfterEach
    void tearDown() {
        executor.shutdown();
    }

    @Test
    void testSuccessfulExecution() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Job job = new Job("1", latch::countDown);

        executor.execute(job);
        assertTrue(latch.await(2, TimeUnit.SECONDS));
        Thread.sleep(100);
        assertEquals(JobState.COMPLETED, job.getState());
    }

    @Test
    void testFailedExecution() throws InterruptedException {
        Job job = new Job("1", () -> {
            throw new RuntimeException("boom");
        });

        executor.execute(job);
        Thread.sleep(500);
        assertEquals(JobState.FAILED, job.getState());
        assertEquals("boom", job.getFailureReason());
    }

    @Test
    void testConcurrentJobsRunOnDifferentThreads() throws InterruptedException {
        int jobCount = 4;
        CountDownLatch allStarted = new CountDownLatch(jobCount);
        CountDownLatch allDone = new CountDownLatch(jobCount);
        AtomicInteger completedCount = new AtomicInteger(0);

        for (int i = 0; i < jobCount; i++) {
            Job job = new Job(String.valueOf(i), () -> {
                allStarted.countDown();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                completedCount.incrementAndGet();
                allDone.countDown();
            });
            executor.execute(job);
        }

        assertTrue(allStarted.await(2, TimeUnit.SECONDS), "All jobs should start concurrently");
        assertTrue(allDone.await(3, TimeUnit.SECONDS), "All jobs should complete");
        assertEquals(jobCount, completedCount.get());
    }

    @Test
    void testDoubleExecutionPrevented() throws InterruptedException {
        AtomicInteger runCount = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(1);

        Job job = new Job("1", () -> {
            runCount.incrementAndGet();
            latch.countDown();
        });

        executor.execute(job);
        executor.execute(job); // should be ignored — state is no longer CREATED

        assertTrue(latch.await(2, TimeUnit.SECONDS));
        Thread.sleep(200);
        assertEquals(1, runCount.get());
    }
}
