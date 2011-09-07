package com.whwqs.webchess.core;

import java.util.*;

import com.whwqs.util.*;

public class ChessBoard implements IPublisher {
	private ChessType[][] boardData = new ChessType[][]{
		{ChessType.³µ,ChessType.Âí,ChessType.Ïà,ChessType.Ê¿,ChessType.Ë§,ChessType.Ê¿,ChessType.Ïà,ChessType.Âí,ChessType.³µ},
		{ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ},
		{ChessType.¿Õ,ChessType.ÅÚ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.ÅÚ,ChessType.¿Õ},
		{ChessType.±ø,ChessType.¿Õ,ChessType.±ø,ChessType.¿Õ,ChessType.±ø,ChessType.¿Õ,ChessType.±ø,ChessType.¿Õ,ChessType.±ø},
		{ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ},
		
		{ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ},
		{ChessType.×ä,ChessType.¿Õ,ChessType.×ä,ChessType.¿Õ,ChessType.×ä,ChessType.¿Õ,ChessType.×ä,ChessType.¿Õ,ChessType.×ä},
		{ChessType.¿Õ,ChessType.³h,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.³h,ChessType.¿Õ},
		{ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ,ChessType.¿Õ},
		{ChessType.Ü‡,ChessType.ñR,ChessType.Ïó,ChessType.ÊË,ChessType.½«,ChessType.ÊË,ChessType.Ïó,ChessType.ñR,ChessType.Ü‡}
	};
	
	public static final String EVENT_DAPU = "´òÆ×°ÚÆå";
	public static final String EVENT_PLAY = "ÏÂÁËÒ»²½";
	public static final String EVENT_UNDO = "»ÚÆå";
	public static final String EVENT_REDO = "²»»ÚÁË";
	public static final String EVENT_END = "ÏÂÍêÁË";
	
	private ChessRules rule = new ChessRules(this);
	
	public void AcceptClicked(int nodeClicked,Boolean isRed)
	{
		if(rule.AcceptClicked(nodeClicked, isRed)){
			
		}
	}
	
	private Boolean isRedGoAhead = true;

	public Boolean getIsRedGoAhead() {
		return isRedGoAhead;
	}
	
	public Boolean IsRedToGo(){
		return (isRedGoAhead && UnDoStack.size()%2==0);
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
		this.isRedGoAhead = isRedGoAhead;
		ReDoStack.clear();
		UnDoStack.clear();
		Notify(new EventBase(ChessBoard.EVENT_DAPU,ToString()));
	}
	
	public void Play(int from,int to)
	{
		ReDoStack.clear();
		UnDoStack.push(new PlayAction(from,to));
		Notify(new EventBase(ChessBoard.EVENT_PLAY,ToString()));
	}
	
	public void UnDo()
	{
		if(!UnDoStack.isEmpty())
		{
			ReDoStack.push(UnDoStack.pop()).UnDo();
			Notify(new EventBase(ChessBoard.EVENT_UNDO,ToString()));
		}
	}
	
	public void ReDo()
	{
		if(!ReDoStack.isEmpty())
		{
			UnDoStack.push(ReDoStack.pop()).ReDo();
			Notify(new EventBase(ChessBoard.EVENT_REDO,ToString()));
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
			boardData[from/9][from%9] = ChessType.¿Õ;
		}
		
		public void UnDo()
		{
			boardData[from/9][from%9] = fromType;
			boardData[to/9][to%9] = ToType;
		}
		
		public void ReDo()
		{
			boardData[to/9][to%9] = boardData[from/9][from%9];
			boardData[from/9][from%9] = ChessType.¿Õ;
		}
	}

	private List<ISubscriber> subscriberList = new ArrayList<ISubscriber>();

	@Override
	public void AddSubscriber(ISubscriber subscriber) {
		// TODO Auto-generated method stub
		subscriberList.add(subscriber);
	}

	@Override
	public void RemoveSubscriber(ISubscriber subscriber) {
		// TODO Auto-generated method stub
		subscriberList.remove(subscriber);
	}

	@Override
	public void Notify(EventBase eventArg) {
		// TODO Auto-generated method stub
		for(ISubscriber subscr :subscriberList)
		{
			subscr.Update(eventArg);
		}
	}
}
