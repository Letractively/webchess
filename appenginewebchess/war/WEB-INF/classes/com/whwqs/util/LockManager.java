package com.whwqs.util;

import java.util.Hashtable;
import java.util.Map;

public class LockManager {
	private static Map<String,Object> Locks = new Hashtable<String,Object>();
	
	public static Object GetLock(String key)
	{
		if(!Locks.containsKey(key))
		{
			synchronized(LockManager.class)
			{
				if(!Locks.containsKey(key))
				{
					Locks.put(key, new Object());
				}
			}
		}
		return Locks.get(key);
	}

}
