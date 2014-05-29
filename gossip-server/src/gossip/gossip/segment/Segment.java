//package gossip.gossip.segment;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.bit.dlde.yuqing.cm.entity.Word;
//
//import com.sun.jna.Library;
//import com.sun.jna.Native;
//
//public class Segment
//{
//	private String dataPath = "";
//	
//	public Segment()
//	{
//		
//	}
//	
//	public Segment(String dataPath)
//	{
//		this.dataPath = dataPath;
//	}
//	
//	public interface CLibrary extends Library
//	{
//		// 定义并初始化接口的静态变量
//		CLibrary Instance = (CLibrary) Native.loadLibrary("NLPIR",
//				CLibrary.class);
//
//		public int NLPIR_Init(String sDataPath, int encoding,
//				String sLicenceCode);
//
//		public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);
//
//		public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,
//				boolean bWeightOut);
//
//		public int NLPIR_AddUserWord(String sWord);
//
//		public int NLPIR_DelUsrWord(String sWord);
//
//		public void NLPIR_Exit();
//	}
//
//	public List<Word> simpleSegPos(String text)
//	{
//		List<Word> words = new ArrayList<Word>();
//		String wholeResult = simpleSegWhole(text);
//		if (wholeResult == null)
//			return null;
//		String[] tokens = wholeResult.split(" ");
//		int size = tokens.length;
//		for (int i = 0; i < size; i++)
//		{
//			int pos = tokens[i].indexOf('/');
//			if (pos < 1)
//				continue;
//			Word word = new Word();
//			word.setText(tokens[i].substring(0, pos));
//			word.setPos(tokens[i].substring(pos + 1));
//			words.add(word);
//		}
//		return words;
//	}
//
//	public String[] simpleSegWord(String text)
//	{
//		String wholeResult = simpleSegWhole(text);
//		if (wholeResult == null)
//			return null;
//		String[] tokens = wholeResult.split(" ");
//		int size = tokens.length;
//		for (int i = 0; i < size; i++)
//		{
//			int pos = tokens[i].indexOf('/');
//			if (pos < 1)
//				continue;
//			tokens[i] = tokens[i].substring(0, pos);
//		}
//		return tokens;
//	}
//
//	public String simpleSegWhole(String text)
//	{
//		int init_flag = CLibrary.Instance.NLPIR_Init(dataPath, 1, "0");
//		if (0 == init_flag)
//		{
//			System.err.println("初始化失败！");
//			return null;
//		}
//
//		String nativeBytes = null;
//		try
//		{
//			nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(text, 1);
//			CLibrary.Instance.NLPIR_Exit();
//			return nativeBytes;
//		}
//		catch (Exception ex)
//		{
//			ex.printStackTrace();
//			return null;
//		}
//	}
//
//	public String[] getKeyWords(String text)
//	{
//		int init_flag = CLibrary.Instance.NLPIR_Init(dataPath, 1, "0");
//		if (0 == init_flag)
//		{
//			System.err.println("初始化失败！");
//			return null;
//		}
//		try
//		{
//			String nativeByte = CLibrary.Instance.NLPIR_GetKeyWords(text, 10,
//					false);
//			if (nativeByte == null)
//				return null;
//			String[] keywords = nativeByte.split("#");
//			CLibrary.Instance.NLPIR_Exit();
//			return keywords;
//
//		}
//		catch (Exception ex)
//		{
//			ex.printStackTrace();
//			return null;
//		}
//	}
//}
