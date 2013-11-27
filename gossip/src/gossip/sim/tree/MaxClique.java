package gossip.sim.tree;

import java.util.Arrays;

public class MaxClique {
	static int[] x; // 当前解
	static int n; // 图G的顶点数
	static int cn; // 当前顶点数
	static int bestn; // 当前最大顶点数
	static int[] bestx; // 当前最优解
	static boolean[][] matrix; // 图G的邻接矩阵

	/**
	 * @param m
	 *            in the graph matrix
	 * @param v
	 *            out the best solution 最优解
	 * @return the best result 最优值
	 */
	public static int maxClique(boolean[][] m, int[] v) {
		matrix = m;
		n = matrix.length;
		x = new int[n];
		cn = 0;
		bestn = 0;
		bestx = v;

		trackback(0);
		return bestn;
	}

	private static void trackback(int i) {
		if (i == n) {
			// 到达叶结点
			for (int j = 0; j < n; j++) {
				bestx[j] = x[j];
			}
			bestn = cn;
			return;
		}

		// 检查顶点 i 与当前团的连接
		boolean connected = true;
		for (int j = 0; j < i; j++) {
			if (x[j] == 1 && !matrix[i][j]) {
				// i 和 j 不相连
				connected = false;
				break;
			}
		}
		if (connected) {
			// 进入左子树
			x[i] = 1;
			cn++;
			trackback(i + 1);
			cn--;
		}
		if (cn + n - i > bestn) {
			// 进入右子树
			x[i] = 0;
			trackback(i + 1);
		}
	}

	private static void test() {
		boolean[][] matrix = { { false, true, true, true, true },
				{ true, false, true, false, true },
				{ true, true, false, false, true },
				{ true, false, false, false, false },
				{ true, true, true, false, false } };
		int[] v = new int[matrix.length];
		int bestn = maxClique(matrix, v);
		System.out.println("bestn is: " + bestn);
		System.out.println(Arrays.toString(bestx));
	}

//	public static void main(String[] args) {
//		test();
//	}
}
