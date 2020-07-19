import java.util.ArrayList;
import java.util.Random;

//Singleton Design Pattern
public class SharedData {
	private static SharedData uniqueData=new SharedData(); //Eager Instantiation
	private static ArrayList<Integer> generatedList=new ArrayList<Integer>();
	
	public boolean[] playerChanceFlags = new boolean[8]; //keeps track of the players that have already had their chance in this turn

	public int announcedNumber; //the number announced by moderator in this turn
	public boolean numberAnnouncedFlag = false;

	public boolean winnerFoundFlag = false; 
	public boolean gameFinishedFlag = false;	
	
	public int generatedIndex=0;
	public Object lock = new Object();
	
	//private constructor as Singleton class
	private SharedData() {
	}
	
	public static SharedData getInstance() {
		return uniqueData;
	}	
	
	//generates the new number to be announced by moderator
	void generateNumber() {
		Random rand = new Random(); 
		announcedNumber=rand.nextInt(51);
		generatedList.add(announcedNumber);
		generatedIndex++;
	}
	
	//checks if there is any player remaining that hasn't got his chance
	public boolean anyPlayerRemaining() {
		for(int i=0;i<8;i++) {
			if(!playerChanceFlags[i]) {
				return true;
			}
		}
		return false;
	}
	
}
