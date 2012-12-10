package gossip.sim.tree;

import java.util.List;

public class TreeNode<T> {

	private int selfId;
	private T t;
	private List<TreeNode<T>> children;
	private TreeNode<T> parent;
	
	
	
	public int getSelfId() {
		return selfId;
	}
	public void setSelfId(int selfId) {
		this.selfId = selfId;
	}
	public T getT() {
		return t;
	}
	public void setT(T t) {
		this.t = t;
	}
	public List<TreeNode<T>> getChildren() {
		return children;
	}
	public void setChildren(List<TreeNode<T>> children) {
		this.children = children;
	}
	public TreeNode<T> getParent() {
		return parent;
	}
	public void setParent(TreeNode<T> parent) {
		this.parent = parent;
	}
	
	
}
