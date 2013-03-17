package gossip.util;

/**
 * 工具类
 * 
 * @author lins 2012-9-3
 */
public class StringUtils {
	public final static int TIME = 0;
	public final static int TITLE = 1;
	public final static int BODY = 2;

	/**
	 * 
	 * @param str
	 *            需要获得被获得摘要的字符串
	 * @param maxLen
	 *            摘要的最大长度
	 * @param type
	 *            TIME = 0;TITLE = 1;BODY = 2;
	 */
	public static String getAbstract(String str, int maxLen, int type) {
		int start = 0;
		int end = Math.min(str.length(), maxLen);
		// 根据类型进行一些额外的处理
		switch (type) {
		case TIME:
			
		case BODY:
			int i = str.indexOf(")");
			if (i == -1 || i > 30)//30是观察到的出版社-记者等信息的最大的长度
				i = str.indexOf("）");
			if (i != -1 && i <= 30)
				start = i + 1;
			str = str.substring(start, end).trim()+"...";
			break;
		default:
			str = str.substring(start, end).trim();
			break;
		}
		return str;
		
	}

}
