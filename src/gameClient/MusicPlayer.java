package gameClient;

import javazoom.jl.player.*;
import java.io.FileInputStream;


/**
 * Music Player Class:
 * this class is in charge of the music played during the game. 
 * will work as a separated thread. 
 */
public class MusicPlayer implements Runnable
{
    private String path;

	public MusicPlayer(String path)	{  	this.path = path;   }
	
    public void play()
    {
        try {
             FileInputStream fis = new FileInputStream(path);
             Player playMP3 = new Player(fis);
             playMP3.play();
             } catch(Exception e) { 	System.out.println(e);    }
    }

	@Override
	public void run(){	play(); }

}