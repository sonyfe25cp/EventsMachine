package gossip.gossip.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;

public class TokenizerUtils {
	
	private static HashSet<String> stopwords;
	
	static{
		stopwords = getStopWords();
	}
	
	public static String[] tokenizer(String body){
		List<Term> terms = ToAnalysis.parse(body);
		String[] res = new String[terms.size()];
		int i = 0;
		for(Term term : terms){
			String tmp = term.getName();
			if(tmp.trim().length() == 0){
				continue;
			}
			if(stopwords.contains(tmp)){
				continue;
			}
			res[i] = tmp;
			i ++;
		}
		if(i != terms.size()){
			String[] newRes = new String[i];
			for(int x = 0 ; x < i; x ++){
				newRes[x] = res[x];
			}
			return newRes;
		}else{
			return res;
		}
		
	}

	public static String[] tokenizerUnique(String[] terms){
		HashSet<String> set = new HashSet<String>();
		for(String tmp : terms){
			set.add(tmp);
		}
		String[] words = new String[set.size()];
		int j = 0;
		for(String tmp : set){
			words[j] = tmp;
			j++;
		}
		return words;
	}
	public static String arrayToString(String[] array){
		String str = "";
		for(String tmp : array){
			str +=(tmp+";");
		}
		return str;
	}
	
	public static HashSet<String> getStopWords(){
		HashSet<String> set = new HashSet<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("/Users/omar/workspace/mavenworkspace/EventsMachine/gossip-server/conf/stopwords")));
			String line = null;
			while(( line = br.readLine())!=null){
				set.add(line);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return set;
	}

	public static boolean hasInteger(String string){
		String regex = "[0-9]+";
		Pattern patter = Pattern.compile(regex);
		Matcher matcher = patter.matcher(string);
		if(matcher.find()){
			return true;
		}else{
			return false;
		}
	}
	
	public static void main(String[] args){
		String str ="我爱北京天安门，天安门上太阳升";
		String  tmp = arrayToString(tokenizer(str));
		System.out.println(tmp);
		
		boolean flag = hasInteger(str);
		System.out.println("hasInteger:"+ flag);
		
//		HashSet<String> hash = getStopWords();
//		for(String word : hash){
//			System.out.println(word);
//		}
		
//		List<Term> terms = ToAnalysis.parse(str);
//		for(Term term : terms){
//			System.out.println(term.getName() + term.getNatrue().);
//		}
		
		KeyWordComputer kwc = new KeyWordComputer(5);
        String title = "维基解密否认斯诺登接受委内瑞拉庇护";
        String content = "有俄罗斯国会议员，9号在社交网站推特表示，美国中情局前雇员斯诺登，已经接受委内瑞拉的庇护，不过推文在发布几分钟后随即删除。俄罗斯当局拒绝发表评论，而一直协助斯诺登的维基解密否认他将投靠委内瑞拉。　　俄罗斯国会国际事务委员会主席普什科夫，在个人推特率先披露斯诺登已接受委内瑞拉的庇护建议，令外界以为斯诺登的动向终于有新进展。　　不过推文在几分钟内旋即被删除，普什科夫澄清他是看到俄罗斯国营电视台的新闻才这样说，而电视台已经作出否认，称普什科夫是误解了新闻内容。　　委内瑞拉驻莫斯科大使馆、俄罗斯总统府发言人、以及外交部都拒绝发表评论。而维基解密就否认斯诺登已正式接受委内瑞拉的庇护，说会在适当时间公布有关决定。　　斯诺登相信目前还在莫斯科谢列梅捷沃机场，已滞留两个多星期。他早前向约20个国家提交庇护申请，委内瑞拉、尼加拉瓜和玻利维亚，先后表示答应，不过斯诺登还没作出决定。　　而另一场外交风波，玻利维亚总统莫拉莱斯的专机上星期被欧洲多国以怀疑斯诺登在机上为由拒绝过境事件，涉事国家之一的西班牙突然转口风，外长马加略]号表示愿意就任何误解致歉，但强调当时当局没有关闭领空或不许专机降落。";
        Collection<Keyword> result = kwc.computeArticleTfidf(title, content);
        System.out.println(result);
        
        List<Term> terms = ToAnalysis.parse("Ansj在北京市的中文分词是一个真正的ict的实现，山东省临沭县的沭河很好.并且加入了自己的一些数据结构和算法的分词.实现了高效率和高准确率的完美结合!");
        new NatureRecognition(terms).recognition() ;
        System.out.println(terms);
        for(Term termTmp : terms){
        	System.out.println(termTmp.getName() + termTmp.getNatrue().natureStr);
        }
	}
}
