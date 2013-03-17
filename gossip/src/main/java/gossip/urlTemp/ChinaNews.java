package gossip.urlTemp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChinaNews {
	
	private String pageUrlReg="http://www\\.chinanews\\.com/gn/[0-9]+/[0-9-]+/[0-9]+\\.shtml";//最终页地址正则
	private Pattern p;
	private Matcher m;
	
	public ChinaNews(){
		p=Pattern.compile(pageUrlReg);
	}
	
	/**
	 * 基本url
	 */
	public List<String> baseUrl(){
		List<String> urls=new ArrayList<String>();
		//http://www.chinanews.com/scroll-news/gn/2012/0710/news.shtml
		Calendar cal = Calendar.getInstance();
		int current_year = cal.get(Calendar.YEAR);
		int current_month = cal.get(Calendar.MONTH )+1;
		int current_day=cal.get(Calendar.DATE);
		
		for(int year=current_year;year>=2010;year--){
			boolean year_flag=current_year==year;
			for(int month=12;month>=1;month--){
				if(year_flag){
					if(month>current_month){
						continue;
					}
				}
				boolean month_flag=current_month==month;
				for(int day=31;day>=1;day--){
					if(year_flag&&month_flag){
						if(day>current_day)
							continue;
					}
					String url="http://www.chinanews.com/scroll-news/gn/"+year+"/"+(month<10?("0"+month):month)+"/"+(day<10?("0"+day):day)+"/news.shtml";
					urls.add(url);
				}
			}
		}
		return urls;
	}
	
	/**
	 * 判断是否是最终页
	 * @param url
	 * @return
	 * Jul 11, 2012
	 */
	public boolean verify(String url){
		m=p.matcher(url);
		boolean flag=m.find();
		return flag;
	}
}
