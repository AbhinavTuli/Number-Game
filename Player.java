import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Random;

public class Player implements Runnable{
	
	//Builder Design Pattern
	public static class Builder{
		private int id;							
		private int ticketCount=0;
		private int numberRange=0;
		
		
		public Builder(int id) {
			this.id=id;
		}
		
		public Builder withTicketCount(int ticketCount){
            this.ticketCount = ticketCount;
            return this;
        }
		
		public Builder withNumberRange(int numberRange){
            this.numberRange = numberRange;
            return this;
        }
		
		public Player build(){
            Player player  = new Player();  

            player.id=this.id;
            player.ticketCount=this.ticketCount;
            player.numberRange=this.numberRange;
            
            player.sharedData= SharedData.getInstance();
            player.totalMatchesFound=0;
            
            return player;
        }
	}
	
	private int[] ticket; //list of the numbers on ticket
	private int ticketCount; //how many total numbers present on each ticket
	private int numberRange; //upper bound of range of numbers on ticket
	private HashMap<Integer,Boolean> isStruckOff; //tracks the numbers that have been struck off i.e. already matched
	private SharedData sharedData;	
	private int id;	//unique identifier for each player						
	private int totalMatchesFound; //count of how many matches found on ticket
	
	
	private Player() {	
	}
	
	public void generatePlayerTicket() {
		Random rand = new Random(); 
		ticket=new int[ticketCount];		
        
		for(int i=0;i<ticketCount;i++) {
			ticket[i]=rand.nextInt(numberRange+1);
		}
		
		isStruckOff=new HashMap<Integer, Boolean>();
        for(int i=0;i<ticketCount;i++) {
        	isStruckOff.put(i, false);
        }
	}
	
	public void printPlayerTicket() {
		BufferedWriter log = new BufferedWriter(new OutputStreamWriter(System.out));

		try{
			log.write("Player"+id+" ticket:");
			log.flush();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		for(int i=0;i<ticket.length;i++) {
			try{
				log.write(" "+ticket[i]);
				log.flush();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		try{
			log.write("\n");
			log.flush();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getTotalMatchesFound() {
		return totalMatchesFound;
	}
	
	//compares currently announced number with all currently remaining numbers on ticket
	public void matchTicket() {
		for(int i = 0; i < ticket.length; i++) {						
			if(sharedData.announcedNumber == ticket[i] && !isStruckOff.get(i)) {
				this.totalMatchesFound++;
				isStruckOff.put(i,true);
				break;
			}
		}
	}
	
	public void run() {
		synchronized(sharedData.lock) {	
			
			while(!sharedData.gameFinishedFlag) {
				
				while(!sharedData.gameFinishedFlag && (!sharedData.numberAnnouncedFlag || sharedData.playerChanceFlags[id])) {
					try {
						sharedData.lock.wait(); //waiting for chance to access the shared resource
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				if(!sharedData.gameFinishedFlag) {										
					matchTicket();
					if(totalMatchesFound == 3 && !sharedData.winnerFoundFlag) {
						sharedData.winnerFoundFlag=true;
						System.out.println("\n***** Game Complete. Player"+id+" won. *****\n");
					}
					
					sharedData.playerChanceFlags[id] = true;
					sharedData.lock.notifyAll(); //wakes all the threads waiting for the shared resource
				}
			}
		}
	}
}
