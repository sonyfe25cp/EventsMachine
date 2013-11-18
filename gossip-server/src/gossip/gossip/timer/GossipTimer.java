package gossip.gossip.timer;

import gossip.gossip.action.AutoDetectEventTask;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class GossipTimer extends QuartzJobBean {

	private AutoDetectEventTask adet;

	protected void executeInternal(JobExecutionContext context)	throws JobExecutionException {
		adet.run(new Date(System.currentTimeMillis()));
	}

	public AutoDetectEventTask getAdet() {
		return adet;
	}

	public void setAdet(AutoDetectEventTask adet) {
		this.adet = adet;
	}
	

}
