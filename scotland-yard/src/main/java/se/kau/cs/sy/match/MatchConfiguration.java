package se.kau.cs.sy.match;

import se.kau.cs.sy.board.Board;

/**
 * This interface defines the getters for the configuration parameters of a match of Scotland Yard.
 * 
 * @author Sebastian Herold
 *
 */
public interface MatchConfiguration {

	/**
	 * The board to be used
	 */
	public Board getBoard();
	
	/**
	 * Set of available starting positions
	 */
	public int[] getStartingPositions();
	
	/**
	 * Number of taxi tickets per detective
	 */
	public int getTaxiTicketsPerDetective();
	
	/**
	 * Number of bus tickets per detective
	 */	
	public int getBusTicketsPerDetective();
	
	/**
	 * Number of underground tickets per detective
	 */
	public int getUndergroundTicketsPerDetective();
	
	/**
	 * Number of black tickets per detective
	 */
	public int getBlackTicketsPerDetective();
	
	/**
	 * Number of taxi tickets for Mr. X 
	 */
	public int getTaxiTicketsForMrX();
	
	/**
	 * Number of bus tickets for Mr. X 
	 */
	public int getBusTicketsForMrX();
	
	/**
	 * Number of underground tickets for Mr. X 
	 */
	public int getUndergroundTicketsForMrX();
	
	/**
	 * Number of black tickets for Mr. X 
	 */
	public int getBlackTicketsForMrX();
	
	/**
	 * Maximal number of turns
	 */
	public int getNumberOfTurns();
	
	/**
	 * Number of detectives 
	 */
	public int getNumberOfDetectives();
	
	/**
	 * Turn numbers after which Mr. X has to reveal his position 
	 */
	public int[] getSurfacingTurns();
}
