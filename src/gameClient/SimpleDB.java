package gameClient;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import utils.SavingScore;


/**
 * SimpleDB class :
 * 
 * this class in charge of extracting all the game Data from the server using a SQL data base. 
 * 
 * the class support extracting the data from the server as a stand alone class (uses a main to get all the info), 
 * or as a static class (in this project used by the resultWindow class to represent the player during the game the his results
 * or his score comparing to all the other players in the server. 
 *
 */
public class SimpleDB  {
	public static final String jdbcUrl="jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
	public static final String jdbcUser="student";
	public static final String jdbcUserPassword="OOP2020student";
	private static int count =0;
	private static int maxlevel=0;
	private static SavingScore data []=new SavingScore[24];


	/**
	 * Simple main for demonstrating the use of the Data-base
	 * @param args
	 */
	public static void main(String[] args) {
		int id1 = 203024989; //Id to enter
		int level = 23;		 //level to display
		LocalDataBase();	
		SavingScore userBestResult []=new SavingScore[24];
		for(int i=0;i<userBestResult.length;i++) {
			userBestResult[i]=new SavingScore(i,0,0);
		}

		PlayerResults(id1,userBestResult);
		System.out.println(printResult(userBestResult));
		System.out.println(placeInClass(id1,userBestResult));
	}
	/** 
	 * print all games from DB.
	 */
	public static void printLog() {
		try {
			Class.forName("com.mysql.jdbc.Driver"); //DB name.
			Connection connection = 
			DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword); //connect to DB
			Statement statement = connection.createStatement();				 
			String allCustomersQuery = "SELECT * FROM Logs;";  //Go to 'Logs' section.
			ResultSet resultSet = statement.executeQuery(allCustomersQuery); //extract all data from logs. 
			//extracting info... 
			while(resultSet.next())
			{
				System.out.println("Id: " + resultSet.getInt("UserID")+","+resultSet.getInt("levelID")+","+resultSet.getInt("moves")+","+resultSet.getDate("time"));
			}
			
			//finish extracting - close SQL DB connection. 
			resultSet.close();
			statement.close();		
			connection.close();		
		}

		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * extracts the Kml file and checks validity 
	 * @param id
	 * @param level
	 * @return
	 */
	public static String getKML(int id, int level) {
		String ans = null;
		String allCustomersQuery = "SELECT * FROM Users where userID="+id+";";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);		
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			if(resultSet!=null && resultSet.next()) {
				ans = resultSet.getString("kml_"+level);
			}
		}
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ans;
	}
	/**
	 * print all users id
	 * @return number of Ids
	 */
	public static int allUsers() {
		int ans = 0;
		String allCustomersQuery = "SELECT * FROM Users;";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);		
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			while(resultSet.next()) {
				System.out.println("Id: " + resultSet.getInt("UserID"));
				ans++;
			}
			resultSet.close();
			statement.close();		
			connection.close();
		}
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ans;
	}
	/** 
	 * extracts the users best results in the game. 
	 * 
	 * @param id 				- player Id
	 * @param userBestResult 	- matrix of user best results
	 */
	public static void PlayerResults(int id,SavingScore userBestResult[])
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs;";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			while(resultSet.next())
			{
				if(resultSet.getInt("UserID")==id) 
				{
					count++;
					int levelID=resultSet.getInt("levelID");
					if(levelID>maxlevel)								
						maxlevel=levelID;
					double score=resultSet.getDouble("score");
					int moves=resultSet.getInt("moves");
					if(score>userBestResult[levelID].getScore()&&score>=data[levelID].getScore()&&moves<=data[levelID].getMoves())
						//update the userBestResult if the score is better and the user past the test
						userBestResult[levelID]=new SavingScore(levelID,score,moves);
				}
			}
			resultSet.close();
			statement.close();		
			connection.close();		
		}

		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * init the data of the criterions to pass the level for each level.
	 */

	private static void LocalDataBase() 
	{
		data[0]=new SavingScore(0,145,290);
		data[1]=new SavingScore(1,450,580);
		data[3]=new SavingScore(3,720,580);
		data[5]=new SavingScore(5,570,500);
		data[9]=new SavingScore(9,510,580);
		data[11]=new SavingScore(11,1050,580);
		data[13]=new SavingScore(13,310,580);
		data[16]=new SavingScore(16,235,290);
		data[19]=new SavingScore(19,250,580);
		data[20]=new SavingScore(20,200,290);
		data[23]=new SavingScore(23,1000,1140);
		for(int i=0;i<data.length;i++)
		{
			if(data[i]==null)
				data[i]=new SavingScore(i,0,Integer.MAX_VALUE);
		}
	}

	/**
	 * print the results given the global data(data,maxlevel,count) and the user best result data. 
	 */
	private static String printResult(SavingScore [] userBestResult) 
	{

		String str="Number of game by the user: "+count+"\n";
		if(userBestResult[maxlevel].getScore()>0)
		{
			if(maxlevel==23)
				str+="The user succeeded all the level"+"\n";
			else
				str+="Your max level is:"+(maxlevel+1)+"\n";
		}
		else
			str+="Your max level is:"+maxlevel+"\n";
		for(int i=0;i<userBestResult.length;i++) 
		{
			if(userBestResult[i].getScore()>0)
				str+=userBestResult[i].toStringLevelScore()+"\n";
			else
				if(i==0||i==1||i==3||i==5||i==9||i==11||i==13||i==16||i==19||i==20||i==23)
					str+="The best score for level "+i+" is 0.0"+"\n";
		}
		return str;
	}
	/**
	 * print our place in class in each level
	 * @param id
	 * @param userBestResult
	 */
	public static String placeInClass(int id,SavingScore userBestResult[]) 
	{
		String str="";
		int levels[]= {0,1,3,5,9,11,13,16,19,20,23};
		for(int i=0;i<levels.length;i++)
		{
			str+=PlaceInClassInLevel(levels[i],id,userBestResult[levels[i]])+"\n";
		}
		return str;
	}
	/**
	 * 
	 * @param level
	 * @param id
	 * @param userBestResult
	 * @return our place in class by the level
	 */
	private static String PlaceInClassInLevel(int level,int id,SavingScore userBestResult) 
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs;";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);	
			HashMap <Integer,Double> betterScore=new HashMap <Integer,Double>();
			while(resultSet.next())
			{
				double score=resultSet.getDouble("score");
				int moves=resultSet.getInt("moves");
				if(resultSet.getInt("userID")!=id&&level==resultSet.getInt("levelID")) 
					//we need to look at all the user but me, and we look for the score for this specific level
				{
					if(score>userBestResult.getScore()&&score>=data[level].getScore()&&moves<=data[level].getMoves()) {
						//only if he past the test
						if(!betterScore.containsKey(resultSet.getInt("userID")))
						{
							//if we no already count him and him to the list of users who get better score.
							betterScore.put(resultSet.getInt("userID"), resultSet.getDouble("score"));
						}
					}
				}
			}
			resultSet.close();
			statement.close();		
			connection.close();

			return "in level "+level+" your place is "+(betterScore.size()+1);
		}
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return "couldnt run the function";

	}
	/**
	 * created for the GUI, print the user max level,how many games he play,the best score on the level he past, and his place in class in each level.
	 * @param level
	 * @param id1
	 */

	public static String StringForGUI(int level,int id1,int wantToSee) 
	{
		try {
		LocalDataBase();
		
		SavingScore userBestResult []=new SavingScore[24];
		for(int i=0;i<userBestResult.length;i++) {
			userBestResult[i]=new SavingScore(i,0,0);
		}
		PlayerResults(id1,userBestResult);
		if(wantToSee==1) {	return printResult(userBestResult);  	}
		if(wantToSee==2) {	return placeInClass(id1,userBestResult);}
		
		return printResult(userBestResult) +placeInClass(id1,userBestResult);
		}catch (Exception e) {
			return "could not connect";
		}
		
	}

}