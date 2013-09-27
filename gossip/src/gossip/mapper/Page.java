package gossip.mapper;

public class Page {
	
	private int pageNo;
	private int pageSize;
	private int numBegin;
	
	

	public Page() {
		super();
	}

	public Page(int pageNo, int pageSize) {
		super();
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.numBegin = pageNo * pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getNumBegin() {
		return numBegin;
	}

	public void setNumBegin(int numBegin) {
		this.numBegin = numBegin;
	}

	
}
