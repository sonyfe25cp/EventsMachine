package gossip.crawl;

import java.util.ArrayList;
import java.util.List;

/**
 * 构造一些基本的链接
 * @author ChenJie
 *
 */
public class CrawlerConductor {
	
	public static List<String> urlConductor(String addressReg,int end){
		return urlConductor(addressReg,1,end);
	}
	
	/**
	 * @param addressReg 数字用()包住
	 * @param begin 起始页码
	 * @param end 结束页码
	 */
	public static List<String> urlConductor(String addressReg,int begin,int end){
		List<String> urlList=new ArrayList<String>();
		for(int i=begin;i<=end;i++){
			String address=addressReg.replaceAll("(.*)",i+"");
			urlList.add(address);
		}
		return urlList;
	}
	/**
	 * @param args
	 * Jul 11, 2012
	 */
	public static void main(String[] args) {
		//http://www.chinanews.com/scroll-news/gn/2012/0710/news.shtml
	}

}
