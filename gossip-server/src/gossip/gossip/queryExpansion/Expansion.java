package gossip.gossip.queryExpansion;

import java.util.ArrayList;
import java.util.List;

public class Expansion {
	private String term;
	private ArrayList<Integer> eventId = new ArrayList<Integer>();
	private ArrayList<Integer> newsId = new ArrayList<Integer>();
	private ArrayList<String> expansionTerms = new ArrayList<String>();
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public ArrayList<Integer> getEventId() {
		return eventId;
	}
	public void setEventId(ArrayList<Integer> eventId) {
		this.eventId = eventId;
	}
	public ArrayList<Integer> getNewsId() {
		return newsId;
	}
	public void setNewsId(ArrayList<Integer> newsId) {
		this.newsId = newsId;
	}
	public ArrayList<String> getExpansionTerms() {
		return expansionTerms;
	}
	public void setExpansionTerms(ArrayList<String> expansionTerms) {
		this.expansionTerms = expansionTerms;
	}
	
	public void addEventId(int id) {
		if (!eventId.contains(id)||eventId.isEmpty())
			eventId.add(id);
	}
	
	public void addEventId(ArrayList<Integer>eventIdList){
		for(int eid:eventIdList){
			if(!eventId.contains(eid)||eventId.isEmpty()||eventId==null){
				eventId.add(eid);
			}
				
		}
	}

	public void addNewsId(int id) {
		if (!newsId.contains(id)||newsId.isEmpty())
			newsId.add(id);
	}

	public void addExpansionTerms(String expansionTerm) {
		if (!expansionTerms.contains(expansionTerm)
				&& (!expansionTerm.equals(term)))
			expansionTerms.add(expansionTerm);
	}

	public void addExpansionTerms(ArrayList<String> expansionTerm) {
		for (String s : expansionTerm)
			if (!expansionTerms.contains(s) && !term.equals(s))
				expansionTerms.add(s);
	}

	public void addNewsId(List<Integer> id) {
		for (int newId : id) {
			if (!newsId.contains(id))
				newsId.add(newId);
		}
	}

}
