package com.whwqs.webchess;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.FileChannel;
import java.util.Date;

import com.whwqs.util.LockManager;
import com.whwqs.webchess.core.ChessBoard;

public class ChessBoardManager {
	private static String root = "c:/chess";	
	
	private static void CreateDirectory(){
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
			CreateDirectory();
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
			    	board.setBoardNumber(key);
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
					board = new ChessBoard(key);
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
		if(!board.getBoardNumber().equals(key)){
			return false;
		}
			
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
	
	private static long forTransfer(File f1,File f2) throws Exception{
        long time=new Date().getTime();
        int length=2097152;
        FileInputStream in=new FileInputStream(f1);
        FileOutputStream out=new FileOutputStream(f2);
        FileChannel inC=in.getChannel();
        FileChannel outC=out.getChannel();
       
        while(true){
            if(inC.position()==inC.size()){
                inC.close();
                outC.close();
                return new Date().getTime()-time;
            }
            if((inC.size()-inC.position())<20971520)
                length=(int)(inC.size()-inC.position());
            else
                length=20971520;
            inC.transferTo(inC.position(),length,outC);
            inC.position(inC.position()+length);
           
        }
    }

	public static void StoreChessBoard(String key) throws Exception{
		String file = GetFileName(key);
		java.io.File boardFile = new java.io.File(file);
		File storeFile = new File(file+"_"+new Date().getTime());
		storeFile.createNewFile();
		if(boardFile.exists()){
			forTransfer(boardFile,storeFile);
		}
	}
}
