package gameClient;

public class mainGame {

	public static void main(String[] args) {

		SimplePlayer play = new SimplePlayer("C:\\Users\\dorge\\eclipse-workspace\\OOP-Ex3\\music.mp3");
		Thread t1 = new Thread(play); 
		
		SimpleGameClient sc = new SimpleGameClient();
		Thread t2 = new Thread(sc); 
		
		//t1.start();
		t2.start();

	}

}
