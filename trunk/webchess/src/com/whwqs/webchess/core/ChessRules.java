package com.whwqs.webchess.core;

public class ChessRules {
	
	private ChessBoard chessBoard;
	
	public ChessRules(ChessBoard qp){
		this.chessBoard = qp;
	}
	
	private int from = -1;
	private int to = -1;
	
	public int getFrom() {
		return from;
	}
	
	public int getTo() {
		return to;
	}	

	public Boolean AcceptClicked(int nodeClicked,Boolean isRedClicked){
		if((chessBoard.IsRedToGo() && !isRedClicked)||(!chessBoard.IsRedToGo()&&isRedClicked))
		{
			return false;
		}
		return true;
	}
}
