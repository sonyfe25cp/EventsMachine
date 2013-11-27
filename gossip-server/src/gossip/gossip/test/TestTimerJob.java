package gossip.gossip.test;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class TestTimerJob extends QuartzJobBean {

	private TestTimerTask testTimerTask;
	
		 
	protected void executeInternal(JobExecutionContext context)	throws JobExecutionException {
	 
		testTimerTask.printSchedulerMessage();
	 
	}

	public TestTimerTask getTestTimerTask() {
		return testTimerTask;
	}
	public void setTestTimerTask(TestTimerTask testTimerTask) {
		this.testTimerTask = testTimerTask;
	}
		
}
