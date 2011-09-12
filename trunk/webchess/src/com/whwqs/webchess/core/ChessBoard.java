package com.whwqs.webchess.core;

import java.util.*;

import com.whwqs.util.*;

public class ChessBoard implements IPublisher {
	private ChessType[][] boardData = new ChessType[][]{
		{ChessType.іµ,ChessType.Вн,ChessType.Па,ChessType.Кї,ChessType.Л§,ChessType.Кї,ChessType.Па,ChessType.Вн,ChessType.іµ},
		{ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ},
		{ChessType.їХ,ChessType.ЕЪ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.ЕЪ,ChessType.їХ},
		{ChessType.±ш,ChessType.їХ,ChessType.±ш,ChessType.їХ,ChessType.±ш,ChessType.їХ,ChessType.±ш,ChessType.їХ,ChessType.±ш},
		{ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ},
		
		{ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ},
		{ChessType.Чд,ChessType.їХ,ChessType.Чд,ChessType.їХ,ChessType.Чд,ChessType.їХ,ChessType.Чд,ChessType.їХ,ChessType.Чд},
		{ChessType.їХ,ChessType.іh,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.іh,ChessType.їХ},
		{ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ},
		{ChessType.Ь‡,ChessType.сR,ChessType.Пу,ChessType.КЛ,ChessType.Ѕ«,ChessType.КЛ,ChessType.Пу,ChessType.сR,ChessType.Ь‡}
	};
	
	private ChessRules rule = new ChessRules(this);
	
	
	public void HandleClicked(int nodeClicked,Boolean isRedClicked,String clickManCurrentBoard)
	{
		rule.AcceptClicked(nodeClicked, isRedClicked,clickManCurrentBoard);
		Notify();
	}
	
	private Boolean isRedGoAhead = true;

	public Boolean getIsRedGoAhead() {
		return isRedGoAhead;
	}
	
	public Boolean IsRedToGo(){
		return (isRedGoAhead && UnDoStack.size()%2==0)||(!isRedGoAhead && UnDoStack.size()%2==1);
	}
	

	public ChessType[][] getBoardData() {
		return boardData;
	}
	
	public String ToString()
	{
		return GetString(boardData);
	}	
	
	private String GetString(ChessType[][] board)
	{
		String s = "";
		for(ChessType[] row :board)
		{
			for(ChessType col:row)
			{
				s+=col.getName();
			}
		}
		return s;
	}
	
	public void SetBoard(String data,Boolean isRedGoAhead)
	{
		for(int i=0;i<90;i++)
		{
			boardData[i/9][i%9]=ChessType.Get(data.substring(i, i+1));
		}
		String s = rule.ValidateChessBoardData(boardData);
		if(s=="")
		{
			this.isRedGoAhead = isRedGoAhead;
			rule.setIsRedGoAhead(isRedGoAhead);
			ReDoStack.clear();
			UnDoStack.clear();			
		}
		Notify();
	}
	
	public void Play(int from,int to)
	{
		ReDoStack.clear();
		UnDoStack.push(new PlayAction(from,to));
	}
	
	public void UnDo()
	{
		if(!UnDoStack.isEmpty())
		{
			ReDoStack.push(UnDoStack.pop()).UnDo();			
		}
	}
	
	public void ReDo()
	{
		if(!ReDoStack.isEmpty())
		{
			UnDoStack.push(ReDoStack.pop()).ReDo();
		}
	}
	
	private Stack<PlayAction> ReDoStack = new Stack<PlayAction>();
	private Stack<PlayAction> UnDoStack = new Stack<PlayAction>();	
	
	
	private class PlayAction
	{
		private ChessType fromType;
		private ChessType ToType;
		private int from;
		private int to;
		PlayAction(int from,int to)
		{
			this.from = from;
			this.to = to;
			this.fromType = boardData[from/9][from%9];
			this.ToType = boardData[to/9][to%9];
			
			boardData[to/9][to%9] = boardData[from/9][from%9];
			boardData[from/9][from%9] = ChessType.їХ;
		}
		
		public void UnDo()
		{
			boardData[from/9][from%9] = fromType;
			boardData[to/9][to%9] = ToType;
		}
		
		public void ReDo()
		{
			boardData[to/9][to%9] = boardData[from/9][from%9];
			boardData[from/9][from%9] = ChessType.їХ;
		}
	}
	
	private Map<String,List<ISubscriber>> subscriberList = new Hashtable<String,List<ISubscriber>>();
	
	@Override
	public void AddSubscriber(String EventName,ISubscriber subscriber) {
		// TODO Auto-generated method stub
		if(!subscriberList.containsKey(EventName))
		{
			subscriberList.put(EventName, new ArrayList<ISubscriber>());
		}
		subscriberList.get(EventName).add(subscriber);
	}

	@Override
	public void RemoveSubscriber(String EventName,ISubscriber subscriber) {
		// TODO Auto-generated method stub
		if(!subscriberList.containsKey(EventName))
		{
			subscriberList.get(EventName).remove(subscriber);
		}
	}

	@Override
	public void Notify(String EventName,EventBase eventArg) {
		// TODO Auto-generated method stub
		if(!subscriberList.containsKey(EventName))
		{
			for(ISubscriber subscr :subscriberList.get(EventName))
			{
				subscr.Update(eventArg);
			}
		}
	}
	
	private void Notify()
	{
		for(ChessEvent ev : rule.EventsList)
		{
			String eventName = ev.getEventName();
			Notify(eventName,ev);
		}
	}
}
