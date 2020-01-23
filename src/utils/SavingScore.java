package utils;
/**
 * Saving Score class : 
 * 
 * this class is in charge of storing and updating the local data from 
 * the SQL Data Base. 
 */
public class SavingScore
{
	private int level;
	private double score; 
	private int moves;
	
	public SavingScore(int level1,double score1,int moves1)
	{
		level=level1;
		score=score1;
		moves=moves1;
	}
	/**
	 * 
	 * @return number of moves in level. 
	 */
	public int getMoves() {
		return moves;
	}
	/**
	 * 
	 * @return level
	 */
	public int getLevel() {
		return level;
	}
	/**
	 * 
	 * @return level score 
	 */
	public double getScore() {
		return score;
	}
	/**
	 * 
	 * @return string- level score
	 */
	public String toStringLevelScore() {
		return "The best score for level "+level+" is "+score;
	}
}