package no.steria.quizzical;

public class Alternative {

	private int aid;
	private String atext;
	
	public Alternative(){
		
	}
	
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
	
	@Override
	public boolean equals(Object obj) {
		boolean isEqual = false;
		if (obj instanceof Alternative){
			Alternative alternative = (Alternative) obj;
			
			if (this.aid == alternative.aid && this.atext.equals(alternative.atext)){
				isEqual = true;
			}
		}
		return isEqual;
	}
	
}
