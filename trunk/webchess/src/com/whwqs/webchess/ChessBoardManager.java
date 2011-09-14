package com.whwqs.webchess;

import java.util.Hashtable;
import java.util.Map;

import com.whwqs.webchess.core.ChessBoard;

public class ChessBoardManager {
	private static String root = "c:/chess";	
	//private static Map<String,ChessBoard> ChessBoards = new Hashtable<String,ChessBoard>();
	static{
		java.io.File rootDir = new java.io.File(root);
		if(!rootDir.exists())
		{
			rootDir.mkdirs();
		}
	}
	
	public static synchronized ChessBoard GetChessBoard(String key)
	{
		if(ChessBoards.containsKey(key))
		{
			
		}
	}
}
