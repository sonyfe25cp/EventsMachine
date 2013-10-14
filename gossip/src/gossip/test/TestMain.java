package gossip.test;

public class TestMain {
	public static void main(String[] args){
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath(); 
		System.out.println(path);
	}

}
