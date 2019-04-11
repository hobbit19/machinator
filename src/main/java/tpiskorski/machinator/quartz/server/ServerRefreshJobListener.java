package tpiskorski.machinator.quartz.server;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.springframework.stereotype.Component;
import tpiskorski.machinator.core.job.Job;
import tpiskorski.machinator.core.job.JobService;
import tpiskorski.machinator.core.job.JobStatus;

import java.time.LocalDateTime;

@Component
public class ServerRefreshJobListener implements JobListener {

    private static final String LISTENER_NAME = "ServerRefreshListener";

    private final JobService jobService;

    public ServerRefreshJobListener(JobService jobService) {
        this.jobService = jobService;
    }

    @Override public String getName() {
        return LISTENER_NAME;
    }

    @Override public void jobToBeExecuted(JobExecutionContext context) {
        JobKey key = context.getJobDetail().getKey();
        if (isServerRefreshJob(context)) {
            Job job = new Job(key.getName());

            job.setDescription("Server monitor");
            job.setStatus(JobStatus.IN_PROGRESS);
            job.setStartTime(LocalDateTime.now());

            jobService.add(job);
        }
    }

    @Override public void jobExecutionVetoed(JobExecutionContext context) {
        if (isServerRefreshJob(context)) {
            Job job = jobService.getLastByDescription("Server monitor");
            job.setStatus(JobStatus.CANCELLED);
        }
    }

    @Override public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        if (isServerRefreshJob(context)) {
            Job job = jobService.getLastByDescription("Server monitor");

            job.setStatus(JobStatus.COMPLETED);
        }
    }

    private boolean isServerRefreshJob(JobExecutionContext context) {
        return context.getJobDetail().getJobClass().equals(ServerRefreshJob.class);
    }
}