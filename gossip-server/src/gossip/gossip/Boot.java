package gossip.gossip;


import java.util.LinkedList;

/**
 * 启动器，各种需要启动的任务的统一
 *@author lins 2012-8-18
 */
public abstract class  Boot extends Thread{
	protected LinkedList<Handler> handlers = new LinkedList<Handler>();
	
	public abstract void process();
	
	public void addHandler(Handler handler){
		handlers.add(handler);
	}
	
	public void removeHandler(Handler handler){
		handlers.remove(handler);
	}
	
	public void run(){
		process();
	}
}
