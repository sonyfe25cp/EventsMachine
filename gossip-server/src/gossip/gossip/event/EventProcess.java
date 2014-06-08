package gossip.gossip.event;

import gossip.model.Event;

public class EventProcess extends Boot {
	private Event event;

	public EventProcess(Event event) {
		this.event = event;
		init();
	}

	public void init() {
		this.addHandler(new EventTitleHandler());
		this.addHandler(new EventSequenceHandler());
		this.addHandler(new EventSummaryHandler());
		this.addHandler(new EventRecommendHandler());
		this.addHandler(new EventEntityRecognitionHandler());
		this.addHandler(new EventKeyWordsHandler());
		this.addHandler(new EmotionHandler());
	}

	@Override
	public void process() {
		for (Handler handler : handlers) {
			handler.handle(event);
		}
		System.out.println("事件：" + event.getTitle());
		System.out.println("emotion: "+ event.getEmotion());
	}
}
