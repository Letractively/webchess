package com.whwqs.webchess;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.whwqs.util.LockManager;
import com.whwqs.webchess.core.ChessBoard;

public class ChessBoardManager {
	private static String roomKey = "roomKey";
	private static String roomValue = "roomValue";
	public static ChessBoard GetChessBoard(String key) 
	{
		synchronized(LockManager.GetLock(key))
		{
			// Get a handle on the datastore itself
			 DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

			 // Lookup data by known key name
			 Entity userEntity;
			 ChessBoard board = null;
			try {
				userEntity = datastore.get(KeyFactory.createKey(roomKey, key));
				board = (ChessBoard)userEntity.getProperty(roomValue);
			} catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
			
			Entity entity = new Entity(key);
			entity.setProperty(roomKey, key);
			entity.setUnindexedProperty(roomValue, board);
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			datastore.put(entity);
			
			r = true;
			return r;
		}
	}

	public static void StoreChessBoard(String key) throws Exception{
		
	}
	
	public static void DeleteChessBoard(String key){
		
		synchronized(LockManager.GetLock(key))
		{
			ChessBoard board = new ChessBoard(key);
			board.SetBoard(ChessBoard.GetString(ChessBoard.GetNormalBoardData()), true);
			Entity entity = new Entity(key);
			entity.setProperty(roomKey, key);
			entity.setUnindexedProperty(roomValue, board);
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			datastore.put(entity);
		}
	}
	
}
