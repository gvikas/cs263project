package cs263project.cs263project;

public class KeyGenerator {

	Integer uniqueID; 
	
	public KeyGenerator(Integer uniqueID){
		this.uniqueID = uniqueID;
	}

	public void setUniqueID(Integer uniqueID) {
		this.uniqueID = uniqueID;
	}


	public Integer getUniqueID(){
		return uniqueID++;
	}
}
