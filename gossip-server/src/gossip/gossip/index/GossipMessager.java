package gossip.gossip.index;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import net.sf.json.JSONObject;
import edu.bit.dlde.utils.DLDELogger;

/**
 * 通过socket发送信息的类
 * 
 * @author lins 2012-8-15
 */
public class GossipMessager {
	/** 对应的操作 **/
	public static final int GET_DOC_BY_ID = 0;
	public static final int GET_NEWS_RANKING = 1;

	private DLDELogger logger;

	public DLDELogger getLogger() {
		return logger;
	}

	public void setLogger(DLDELogger logger) {
		this.logger = logger;
	}

	String defaultIp;
	int defaultPort;

	public String getDefaultIp() {
		return defaultIp;
	}

	public void setDefaultIp(String defaultIp) {
		this.defaultIp = defaultIp;
	}

	public int getDefaultPort() {
		return defaultPort;
	}

	public void setDefaultPort(int defaultPort) {
		this.defaultPort = defaultPort;
	}

	/**
	 * 使用默认的ip和端口发信息
	 * 
	 * @param jsonObj
	 *            被包装成JSONObject的数据。
	 * @param operation
	 *            当前的操作有：gossip.gossipMessager.GET_DOC_BY_ID。
	 */
	public String sendMessage(JSONObject jsonObj, int operation) {
		return sendMessage(defaultIp, defaultPort, jsonObj, operation);
	}

	/**
	 * 阻塞式地发送信息到指定ip和端口，并且获得响应结果。超时时间为10s。
	 * 
	 * @param jsonObj
	 *            被包装成JSONObject的数据。
	 * @param operation
	 *            当前的操作有：gossip.gossipMessager.GET_DOC_BY_ID。
	 * @param ip
	 *            指定服务所在的ip
	 * @param port
	 *            指定服务所在的端口
	 * @return 返回string型的结果，并未强制要求为json类型
	 */
	public String sendMessage(String ip, int port, JSONObject jsonObj,
			int operation) {
		if (jsonObj == null || ip == null || ip.equals(""))// 由于ipv6的存在，额先不考虑正则验证ip
			return null;
		jsonObj.put("operation", operation);

		Socket client = null;
		DataOutputStream out = null;
		String response = "";
		try {
			/** 打开socket **/
			client = new Socket(ip, port);
			client.setSoTimeout(10000);
			out = new DataOutputStream((client.getOutputStream()));

			/** 向socket写数据 **/
			long t1 = System.currentTimeMillis();
			logger.debug("request body:" + jsonObj.toString());
			byte[] request = jsonObj.toString().getBytes();
			logger.debug("request length:" + request.length);
			out.write(request);
			out.flush();
			client.shutdownOutput();

			/** 从socket读数据 **/
			BufferedReader br = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			String str = br.readLine();
			StringBuilder sb = new StringBuilder();
			while (str != null) {
				sb.append(str);
				logger.info(str);
				str = br.readLine();
			}
			br.close();

			response = sb.toString();

			long t2 = System.currentTimeMillis();

			long tmp = t2 - t1;

			out.close();
			client.close();
			logger.debug("use:" + tmp + "mills");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return response;
	}
}
