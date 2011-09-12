package com.whwqs.util;

public interface IPublisher {
	void AddSubscriber(String EventName,ISubscriber subscriber);
	void RemoveSubscriber(String EventName,ISubscriber subscriber);
	void Notify(String EventName,EventBase eventArg);
}
