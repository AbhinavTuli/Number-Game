public class Moderator implements Runnable {
	
	private SharedData sharedData; 
	
	public Moderator() {
		sharedData = SharedData.getInstance();	
	}

	public void run() {
		synchronized(sharedData.lock) {
			
			while (!sharedData.winnerFoundFlag && sharedData.generatedIndex<10) { //moderator generates numbers till winner not found
				sharedData.numberAnnouncedFlag = false;
				
				//Moderator will announce number after sleep
				try {
					Thread.sleep(800);
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				//reset the chance flags for every new number
				sharedData.playerChanceFlags=new boolean[20];
				sharedData.generateNumber();
				sharedData.numberAnnouncedFlag = true;
				//notifies the player threads
				sharedData.lock.notifyAll();
				
				//wait for all player threads to finish
				while(sharedData.anyPlayerRemaining()) {
					try {
						sharedData.lock.wait(); 
					} 
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
			if(!sharedData.winnerFoundFlag) {
				System.out.println("\n***** Game complete. No winners found. *****\n");
			}
			
			sharedData.gameFinishedFlag = true; 
			sharedData.lock.notifyAll(); 
		}
	}

}
