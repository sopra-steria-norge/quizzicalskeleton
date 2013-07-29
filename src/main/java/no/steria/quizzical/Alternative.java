package no.steria.quizzical;

public class Alternative {

	private int aid;
	private String atext;
	
	public Alternative(int aid, String atext){
		this.aid = aid;
		this.atext = atext;
	}
	
	public int getAid(){
		return aid;
	}
	
	public String getAtext(){
		return atext;
	}
	
}
