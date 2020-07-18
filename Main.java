
public class Main {
	
	public static void main(String[] args) {
//		//getting instance of singleton class SharedData
//		final SharedData sharedData  = SharedData.getInstance();
		
		final Moderator mod= new Moderator();
		Thread modThread  = new Thread(mod);

		final Player[] players=new Player[20];
		Thread[] playerThreads=new Thread[20];

		for(int id=0;id<20;id++) {
			players[id]=new Player.Builder(id).withTicketCount(10).withNumberRange(50).build(); 
		}
		
		for (Player player : players) {
            player.generatePlayerTicket();
            player.printPlayerTicket();
		}
		System.out.println();
		
		//creating separate thread for each player
		for(int id=0;id<20;id++) {
			playerThreads[id]=new Thread(players[id]);
		}
		
		//starting all threads
		modThread.start();
		
		for(int id=0;id<20;id++) {
			playerThreads[id].start();
		}
		
		//waiting for threads to stop running
		//this will happen when the game gets over
		for(int id=0;id<20;id++) {
			try {
				playerThreads[id].join();
				System.out.println("No. of matches for Player"+id+" is "+players[id].getTotalMatchesFound());
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
