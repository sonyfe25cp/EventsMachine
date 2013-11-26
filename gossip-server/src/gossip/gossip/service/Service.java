package gossip.gossip.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;


public class Service {

	public SqlSession session;
	
	public Service(){
		getSqlSession();
	}
	private void getSqlSession(){
		String resource = "conf/mybatis.xml";
		File dir = new File("conf");
		for(File file : dir.listFiles()){
			System.out.println(file.getName());
		}
		
		InputStream inputStream;
		SqlSessionFactory sqlSessionFactory = null;
		try {
			inputStream = Resources.getResourceAsStream(resource);
			sqlSessionFactory = new SqlSessionFactoryBuilder()
					.build(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.session = sqlSessionFactory.openSession();
	}
}
