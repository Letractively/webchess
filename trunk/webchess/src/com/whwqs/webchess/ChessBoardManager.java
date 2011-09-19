package com.whwqs.webchess;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import com.whwqs.util.LockManager;
import com.whwqs.webchess.core.ChessBoard;

public class ChessBoardManager {
	private static String root = "c:/chess";	
	
	static{
		java.io.File rootDir = new java.io.File(root);
		if(!rootDir.exists())
		{
			rootDir.mkdirs();
		}
	}
	
	private static String GetFileName(String key)
	{
		return root + "/"+key;
	}
	
	public static ChessBoard GetChessBoard(String key) 
	{
		synchronized(LockManager.GetLock(key))
		{
			String file = GetFileName(key);
			java.io.File boardFile = new java.io.File(file);
			ChessBoard board = null;
			if(boardFile.exists())
			{
				try
				{
					FileInputStream fis = null;
			    	ObjectInputStream  in = null;
			    	fis = new FileInputStream(file);
			    	in = new ObjectInputStream (fis);
			    	board = (ChessBoard)in.readObject();
			    	in.close();
				}
				catch(IOException ex)
				 {
					ex.printStackTrace();
				 }
			    catch(ClassNotFoundException ex)
			    {
			    	ex.printStackTrace();
			    }
			}
			else
			{
				try
				{
					board = new ChessBoard();
					FileOutputStream fos = null;
				    ObjectOutputStream out = null;
				    fos = new FileOutputStream(file);
			    	out = new ObjectOutputStream(fos);
			    	out.writeObject(board);
			    	out.close();
				}
				catch(IOException ex)
				 {
					ex.printStackTrace();
				 }
			}
			return board;
		}
	}
	
	public static Boolean SetChessBoard(String key,ChessBoard board) 
	{
		synchronized(LockManager.GetLock(key))
		{
			Boolean r = false;
			try
			{
				FileOutputStream fos = null;
			    ObjectOutputStream out = null;
			    fos = new FileOutputStream(GetFileName(key));
		    	out = new ObjectOutputStream(fos);
		    	out.writeObject(board);
		    	out.close();
		    	r = true;
			}
			catch(IOException ex)
			 {
				ex.printStackTrace();
			 }
			return r;
		}
	}
}
