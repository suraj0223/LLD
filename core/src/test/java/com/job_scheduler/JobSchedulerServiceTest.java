package com.job_scheduler;

import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class JobSchedulerServiceTest {

    @AfterEach
    void tearDown() {
        JobSchedulerService.getInstance().shutdown();
    }

    @Test
    void testSingletonInstance() {
        JobSchedulerService first = JobSchedulerService.getInstance();
        JobSchedulerService second = JobSchedulerService.getInstance();
        assertSame(first, second);
    }

    @Test
    void testSubmitAndExecuteJob() throws InterruptedException {
        JobSchedulerService scheduler = JobSchedulerService.getInstance();
        CountDownLatch latch = new CountDownLatch(1);

        Job job = scheduler.submitJob(new Job("1234", latch::countDown));
        assertNotNull(job.getJobId());
        assertEquals(JobState.CREATED, job.getState());

        assertTrue(latch.await(3, TimeUnit.SECONDS));
        Thread.sleep(200);
        assertEquals(JobState.COMPLETED, job.getState());
    }
}
