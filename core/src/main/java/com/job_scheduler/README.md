## Designing Job Schedular 

### Requirements 


### Core Entities

- JobSchedulerService 
- JobStore
- JobExecutor
- JobDispatcher

### Actual Flow

1. Client(Main) create an instance of `JobSchedulerService`
2. Client(Main) create a job and `submit` it to `jobSchedularService`
3. `JobSchedularService` save jobs to `JobStore`, so that it does not lost.  
4. `JobDispatcher` gets instantiated when we create an instance of `JobSchedularService` and pulls jobs from `JobStore` at `ScheduledFixed` rate and Submit that Job to `JobExecutor`
5. `JobExecutor` have fixed set of thread worker pool, which actually `execute` jobs.

### Class diagram 

```text
# JobSchedulerService
  - jobStore: JobStore
  - jobExecutor: JobExecutor
  - jobDispatcher(jobStore, jobExecutor) : JobDispatcher
  
  + getInstance()
  + submitJob(Runnable task) -> Job
  + shutdown() // dispatchers and executors 
  
# JobDispatcher
  - schedular: ScheduledExecutorService // create a Executors.newSingleThreadScheduledExecutor() and instantiate `scheduleAtFixedRate` when start() is called
  + start() // starts the dispatcher
  + stops() // stops the dispatcher
  - dispatchCycle() // keep monitoring the jobstore and submit queued job to executors
  
# JobExecutor 
  + execute(job: Job)
  
# JobStore 
  + getJobById(jobId)
  + getJobByStatus(JobStatus: enum) -> List<Job> jobs // all jobs based on status
  
# Job
  - id 
  - name 
  - Runnable task 
  - JobStatus: enum


# enum JobStatus 
  - SUBMITTED
  - RUNNING
  - COMPLETED
```