package gossip.utils;

import edu.bit.dlde.utils.DLDELogger;

public class DateException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DLDELogger logger = new DLDELogger();
	public String info;
	public DateException(String info){
		this.info=info;
	}
	public void message(){
		logger.error(info);
	}
	
}
