public class Moderator implements Runnable {
	
	private SharedData sharedData; 
	
	public Moderator() {
		sharedData = SharedData.getInstance();	
	}

	public void run() {
		synchronized(sharedData.lock) {
			
			while (!sharedData.winnerFoundFlag && sharedData.generatedIndex<10) { //moderator generates numbers till winner not found
				sharedData.numberAnnouncedFlag = false;
				
				//Moderator will announce number after some delay
				try {
					Thread.sleep(800);
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				sharedData.playerChanceFlags=new boolean[8]; //reset the player chance flags for every new number
				
				sharedData.generateNumber();
				System.out.println("Moderator generated: "+sharedData.announcedNumber);
				
				sharedData.numberAnnouncedFlag = true;
				sharedData.lock.notifyAll(); //wakes all the player threads waiting for the shared resource
				
				//wait for all player threads to finish
				while(sharedData.anyPlayerRemaining()) {
					try {
						sharedData.lock.wait(); //waiting for chance to access the shared resource
					} 
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
			if(!sharedData.winnerFoundFlag) {
				System.out.println("\n***** Game complete. No winners found. *****\n");
			}
			
			//set gameFinishedFlag so that player threads also terminate
			sharedData.gameFinishedFlag = true; 
			sharedData.lock.notifyAll(); //wakes all the player threads waiting for the shared resource
		}
	}

}
