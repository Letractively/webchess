package com.whwqs.webchess.core;

import java.util.*;

import com.whwqs.util.*;

public class ChessBoard implements IPublisher {
	private ChessType[][] boardData = new ChessType[][]{
		{ChessType.��,ChessType.��,ChessType.��,ChessType.ʿ,ChessType.˧,ChessType.ʿ,ChessType.��,ChessType.��,ChessType.��},
		{ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��},
		{ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��},
		{ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��},
		{ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��},
		
		{ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��},
		{ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��},
		{ChessType.��,ChessType.�h,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.�h,ChessType.��},
		{ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��},
		{ChessType.܇,ChessType.�R,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.�R,ChessType.܇}
	};

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
	
	public void SetBoard(String data)
	{
		for(int i=0;i<90;i++)
		{
			boardData[i/9][i%9]=ChessType.Get(data.substring(i, i+1));
		}
		Notify(null);
	}
	
	public void Play(int from,int to)
	{
		ReDoStack.clear();
		UnDoStack.push(new PlayAction(from,to));
		Notify(null);
	}
	
	public void UnDo()
	{
		if(!UnDoStack.isEmpty())
		{
			ReDoStack.push(UnDoStack.pop()).UnDo();
			Notify(null);
		}
	}
	
	public void ReDo()
	{
		if(!ReDoStack.isEmpty())
		{
			UnDoStack.push(ReDoStack.pop()).ReDo();
			Notify(null);
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
			boardData[from/9][from%9] = ChessType.��;
		}
		
		public void UnDo()
		{
			boardData[from/9][from%9] = fromType;
			boardData[to/9][to%9] = ToType;
		}
		
		public void ReDo()
		{
			boardData[to/9][to%9] = boardData[from/9][from%9];
			boardData[from/9][from%9] = ChessType.��;
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
		EventBase evt = new EventBase(ToString());
		for(ISubscriber subscr :subscriberList)
		{
			subscr.Update(evt);
		}
	}
}
