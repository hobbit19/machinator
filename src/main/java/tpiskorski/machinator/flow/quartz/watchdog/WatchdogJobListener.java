package tpiskorski.machinator.flow.quartz.watchdog;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

public class WatchdogJobListener implements JobListener {
    @Override public String getName() {
        return null;
    }

    @Override public void jobToBeExecuted(JobExecutionContext context) {

    }

    @Override public void jobExecutionVetoed(JobExecutionContext context) {

    }

    @Override public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {

    }
}
