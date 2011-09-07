package com.whwqs.util;

public interface IPublisher {
	void AddSubscriber(ISubscriber subscriber);
	void RemoveSubscriber(ISubscriber subscriber);
	void Notify(EventBase eventArg);
}
