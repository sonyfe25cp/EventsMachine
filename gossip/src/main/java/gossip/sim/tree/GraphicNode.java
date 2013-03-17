package gossip.sim.tree;

import java.util.HashMap;
import java.util.Map;

public class GraphicNode {

	private int selfId;
	private String selfName;
	private Map<GraphicNode,Double> children;//key是节点,weight是边的权重
	
	public GraphicNode(int selfId) {
		super();
		this.selfId = selfId;
	}

	public GraphicNode(int selfId, String selfName) {
		super();
		this.selfId = selfId;
		this.selfName = selfName;
	}

	public void addChild(GraphicNode child,double weight){
		children=children==null?new HashMap<GraphicNode,Double>():children;
		children.put(child, weight);
	}
	
	public int getSelfId() {
		return selfId;
	}
	public void setSelfId(int selfId) {
		this.selfId = selfId;
	}
	public String getSelfName() {
		return selfName;
	}
	public void setSelfName(String selfName) {
		this.selfName = selfName;
	}
	public Map<GraphicNode, Double> getChildren() {
		return children;
	}
	public void setChildren(Map<GraphicNode, Double> children) {
		this.children = children;
	}
	
	
}
