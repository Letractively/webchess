package com.whwqs.webchess;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.whwqs.util.LockManager;
import com.whwqs.webchess.core.ChessBoard;
import com.google.appengine.api.datastore.Blob;

public class ChessBoardManager {
	private static String roomKey = "roomKey";
	private static String roomValue = "roomValue";
	private static String roomSort = "roomSort";
	
	private static Entity Get(String key){
		 DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		 Query query = new Query(roomKey);
		 query.addFilter(roomKey,  Query.FilterOperator.EQUAL, key);
		 query.addSort(roomSort, SortDirection.DESCENDING);
		 for(Entity result : datastore.prepare(query).asIterable()){
			 return result;
		 }
		 return null;
	}
	
	private static java.lang.Object ByteToObject(byte[] bytes) {  
	    java.lang.Object obj=null;
	    try {  
	    //bytearray to object  
	    ByteArrayInputStream bi = new ByteArrayInputStream(bytes);  
	    ObjectInputStream oi = new ObjectInputStream(bi);  
	  
	    obj = oi.readObject();  
	  
	    bi.close();  
	    oi.close();  
	    }  
	    catch(Exception e) {  
	        System.out.println("translation"+e.getMessage());  
	        e.printStackTrace();  
	    }  
	    return obj;  
	}  
	private static byte[] ObjectToByte(java.lang.Object obj) {  
	    byte[] bytes=null;
	    try  {  
	        //object to bytearray  
	        ByteArrayOutputStream bo = new ByteArrayOutputStream();  
	        ObjectOutputStream oo = new ObjectOutputStream(bo);  
	        oo.writeObject(obj);  
	  
	        bytes = bo.toByteArray();  
	  
	        bo.close();  
	        oo.close();      
	    }  
	    catch(Exception e) {  
	        System.out.println("translation"+e.getMessage());  
	        e.printStackTrace();  
	    }  
	    return(bytes);  
	}
	
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
				userEntity = Get(key);
				board = (ChessBoard)ByteToObject(((Blob) userEntity.getProperty(roomValue)).getBytes());
				board.init();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				board = new ChessBoard(key);				
				Entity entity = new Entity(roomKey);
				entity.setProperty(roomKey, key);
				entity.setProperty(roomSort,new Date().getTime());
				entity.setUnindexedProperty(roomValue, new Blob(ObjectToByte(board)));
				datastore.put(entity);
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
			
			Entity entity = new Entity(roomKey);
			entity.setProperty(roomKey, key);
			entity.setProperty(roomSort,new Date().getTime());
			entity.setUnindexedProperty(roomValue,new Blob( ObjectToByte(board)));
			
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
			Entity entity = new Entity(roomKey);
			entity.setProperty(roomKey, key);
			entity.setProperty(roomSort,new Date().getTime());
			entity.setUnindexedProperty(roomValue,new Blob( ObjectToByte(board)));
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			datastore.put(entity);
		}
	}
	
}
