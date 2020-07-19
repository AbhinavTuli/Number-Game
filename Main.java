public class Main {
	public static void main(String[] args) {
		System.out.println("***** Starting an 8 player game *****\n");
		final Moderator mod= new Moderator();
		Thread modThread  = new Thread(mod);

		final Player[] players=new Player[8];
		Thread[] playerThreads=new Thread[8];

		for(int id=0;id<8;id++) {
			players[id]=new Player.Builder(id).withTicketCount(10).withNumberRange(50).build(); 
		}
		
		for (Player player : players) {
            player.generatePlayerTicket();
            player.printPlayerTicket();
		}
		System.out.println();
		
		//creating separate thread for each player
		for(int id=0;id<8;id++) {
			try {
				playerThreads[id]=new Thread(players[id]);
			}	
			catch(IndexOutOfBoundsException e){
				System.out.println(e);
			}
		}
		
		//starting all threads
		modThread.start();
		
		for(int id=0;id<8;id++) {
			playerThreads[id].start();
		}
		
		//waiting for threads to stop running
		//this will happen when the game gets over
		for(int id=0;id<8;id++) {
			try {
				playerThreads[id].join();
				System.out.println("No. of matches for Player"+id+": "+players[id].getTotalMatchesFound());
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
