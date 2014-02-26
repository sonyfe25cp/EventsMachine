package gossip.gossip.queryExpansion;

import gossip.gossip.utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

public class FeatureCompute {
	private ExpansionTerm exTerm;
	private DataSource dataSource;
	private int termTotalCountC;//语料集中的词组数
	private int termTotalCountR;//相关文档集中的词组数
	
	public FeatureCompute(ExpansionTerm exTerm){
		this.exTerm = exTerm;
	}
	
	public void computeTFR(List<Integer> docIds){
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			stat = conn.createStatement();

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void init() {
		if (dataSource == null) {
			dataSource = DatabaseUtils.getInstance();
		}
	}


	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public ExpansionTerm getExTerm() {
		return exTerm;
	}

	public void setExTerm(ExpansionTerm exTerm) {
		this.exTerm = exTerm;
	}

	public int getTermTotalCountC() {
		return termTotalCountC;
	}

	public void setTermTotalCountC(int termTotalCountC) {
		this.termTotalCountC = termTotalCountC;
	}

	public int getTermTotalCountR() {
		return termTotalCountR;
	}

	public void setTermTotalCountR(int termTotalCountR) {
		this.termTotalCountR = termTotalCountR;
	}
	
	

}
