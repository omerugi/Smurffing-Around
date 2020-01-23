package gameClient;

import java.util.List;

import Server.game_service;

public class GameServer
{

	private List<String> fruits;
	private int moves;
	private int grade;
	private int robots;
	private String graph;
	private game_service game;
	private int level;
	public GameServer() {;}
	
	public GameServer(List<String> fruits, int moves, int grade, int robots, String graph, game_service game,int level)
	{
		this.fruits=fruits;
		this.moves=moves;
		this.grade=grade;
		this.robots=robots;
		this.graph=graph;
		this.game = game;
		this.level = level;
	}
	
	public int getLevel()
	{
		return this.level;
	}
	
	public List<String> getFruit()
	{
		return this.fruits;
	}
	
	public int getMoves()
	{
		return this.moves;
	}
	
	public int getGrade()
	{
		return this.grade;
	}
	
	public int getRobots()
	{
		return this.robots;
	}
	
	public String getGraph()
	{
		return this.graph;
	}
	
	public game_service getGame() 
	{
		return this.game;
	}
}