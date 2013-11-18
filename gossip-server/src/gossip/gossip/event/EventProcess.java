package gossip.gossip.event;

import gossip.model.Event;


public class EventProcess extends Boot{
	private Event event;
	public EventProcess(Event event){
		this.event = event;
		init();
	}
	public void init(){
		this.addHandler(new EventTitleHandler());
		this.addHandler(new EventSequenceHandler());
		this.addHandler(new EventSummaryHandler());
		this.addHandler(new EventRecommendHandler());
		this.addHandler(new EventLocationHandler());
		this.addHandler(new EventKeyWordsHandler());
	}
	

	@Override
	public void process() {
		for(Handler handler:handlers){
			handler.handle(event);
		}
	}
	
	

}
