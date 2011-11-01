package com.whwqs.util;

import java.io.Serializable;
import java.util.EventObject;

@SuppressWarnings("serial")
public class EventBase extends EventObject implements Serializable {
	
	public EventBase(String eventName,Object arg0)
	{
		super(arg0);
		this.eventName = eventName;
	}
	
	private String eventName;
	
	public String getEventName()
	{
		return eventName;
	}
	
}
