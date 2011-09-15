package com.whwqs.webchess;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.Map;

import com.whwqs.webchess.core.ChessBoard;

public class ChessBoardManager {
	private static String root = "c:/chess";	
	private static Map<String,Object> Locks = new Hashtable<String,Object>();
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
	
	private static Object GetLock(String key)
	{
		if(!Locks.containsKey(key))
		{
			synchronized(ChessBoardManager.class)
			{
				if(!Locks.containsKey(key))
				{
					Locks.put(key, new Object());
				}
			}
		}
		return Locks.get(key);
	}
	
	public static ChessBoard GetChessBoard(String key) 
	{
		synchronized(GetLock(key))
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
		synchronized(GetLock(key))
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
