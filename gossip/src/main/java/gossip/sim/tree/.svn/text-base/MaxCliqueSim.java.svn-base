package gossip.sim.tree;
//package edu.bit.dlde.sim.tree;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map.Entry;
//
//import edu.bit.dlde.sim.SimResult;
//
//public class MaxCliqueSim {
//
//	static int[] x; // 当前解
//	static int n; // 图G的顶点数
//	static int cn; // 当前顶点数
//	static int bestn; // 当前最大顶点数
//	static int[] bestx; // 当前最优解
//	static boolean[][] matrix; // 图G的邻接矩阵
//
//	/**
//	 * @param m  in the graph matrix
//	 * @param v  out the best solution 最优解
//	 * @return the best result 最优值
//	 */
//	public static int maxClique(boolean[][] m, int[] v) {
//		matrix = m;
//		n = matrix.length;
//		x = new int[n];
//		cn = 0;
//		bestn = 0;
//		bestx = v;
//
//		trackback(0);
//		return bestn;
//	}
//
//	private static void trackback(int i) {
//		if (i == n) {
//			// 到达叶结点
//			for (int j = 0; j < n; j++) {
//				bestx[j] = x[j];
//			}
//			bestn = cn;
//			return;
//		}
//
//		// 检查顶点 i 与当前团的连接
//		boolean connected = true;
//		for (int j = 0; j < i; j++) {
//			if (x[j] == 1 && !matrix[i][j]) {
//				// i 和 j 不相连
//				connected = false;
//				break;
//			}
//		}
//		if (connected) {
//			// 进入左子树
//			x[i] = 1;
//			cn++;
//			trackback(i + 1);
//			cn--;
//		}
//		if (cn + n - i > bestn) {
//			// 进入右子树
//			x[i] = 0;
//			trackback(i + 1);
//		}
//	}
//	
//	public HashMap<Integer,List<Integer>> addNodes(List<SimResult> slist){
//		HashMap<Integer,List<Integer>> vset=new HashMap<Integer,List<Integer>>();
//		for(SimResult sr:slist){
//			int doc1=sr.getDoc1();
//			int doc2=sr.getDoc2();
//			List<Integer> children=null;
//			if(vset.containsKey(doc1)){
//				children=vset.get(doc1);
//			}else{
//				children=new ArrayList<Integer>();
//			}
//			
//			children.add(doc2);
//			vset.put(doc1, children);
//			if(vset.containsKey(doc2)){
//				children=vset.get(doc2);
//			}else{
//				children=new ArrayList<Integer>();
//			}
//			children.add(doc1);
//			vset.put(doc2, children);
//		}
//		return vset;
//	}
//	LinkedList ll;
//	
//	public void dd(){
//		ll.add
//	}
//
//	public GraphicNode[][] init(HashMap<Integer,List<Integer>> map){
//		int size=map.size();
//		GraphicNode[][] graphic=new GraphicNode[size][size];
//		
//		for(Entry<Integer,List<Integer>> entry:map.entrySet()){
//			int doc1=entry.getKey();
//			
//		}
//		
//		return graphic;
//	}
//
//
////	public void addNode(SimResult sresult){
////		
////		if(nlist==null){
////			nlist=new HashMap<Integer,GraphicNode>();
////		}
////		
////		GraphicNode node=null;
////		if(nlist.containsKey(sresult.getDoc1())){//包含此点
////			node=nlist.get(sresult.getDoc1());
////		}else{
////			node=new GraphicNode(sresult.getDoc1());
////		}
////		node.addChild(new GraphicNode(sresult.getDoc2()), sresult.getSim());
////		nlist.put(sresult.getDoc1(), node);
////	}
//	
//	
//	
//	
//	
//	/**
//	 * @param args
//	 * Jul 17, 2012
//	 */
//	public static void main(String[] args) {
//		
//	}
//
//}
