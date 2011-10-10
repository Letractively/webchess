package com.whwqs.webchess.core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

import com.whwqs.util.*;
import com.whwqs.webchess.ChessBoardManager;

public class ChessBoard implements IPublisher,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static ChessType[][] GetNormalBoardData(){
		return new ChessType[][]{
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
	}
	
	public static String GetString(ChessType[][] board)
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
	
	private String boardNumber = null;
	
	public int GetFrom(){
		return rule.getHoldNode();
	}
	
	public int GetTo(){
		return rule.getMoveToNode();
	}
	
	public String getBoardNumber() {
		return boardNumber;
	}

	public void setBoardNumber(String boardNumber) {
		if(this.boardNumber==null)
		{
			this.boardNumber = boardNumber;
		}
	}
	
	public List<ChessEvent> GetEventList(){
		return rule.getEventsList();
	}
	
	private ChessRules rule;
	
	public ChessBoard(String boardKey){
		rule = new ChessRules(this);
		this.boardNumber = boardKey;
		init();
	}
	
	private void persist(){
		ChessBoardManager.SetChessBoard(boardNumber, this);
	}

	private void init()
	{		
		subscriberList = new Hashtable<String,List<ISubscriber>>();
		AddSubscriber(ChessEvent.CHECKRESULT_INIT_CHECK,new ISubscriber(){
			public void Update(EventBase eventArg)
			{
				ChessEvent ev = (ChessEvent)eventArg;
				if(!ev.getWrongNodesPositionData().isEmpty())
				{
					persist();
				}
			}
		});
		AddSubscriber(ChessEvent.CHECKRESULT_PLAYOK_BLACK,new ISubscriber(){
			public void Update(EventBase eventArg)
			{
				ChessEvent ev = (ChessEvent)eventArg;
				Play(ev.getFromNode(),ev.getToNode());
				ev.setChessBoardData(ToString());
				persist();
			}
		});
		AddSubscriber(ChessEvent.CHECKRESULT_PLAYOK_RED,new ISubscriber(){
			public void Update(EventBase eventArg)
			{
				ChessEvent ev = (ChessEvent)eventArg;
				Play(ev.getFromNode(),ev.getToNode());
				ev.setChessBoardData(ToString());
				persist();
			}
		});
		AddSubscriber(ChessEvent.CHECKRESULT_CHANGEHOLDNODE_BLACK,new ISubscriber(){
			public void Update(EventBase eventArg)
			{				
				persist();
			}
		});
		AddSubscriber(ChessEvent.CHECKRESULT_CHANGEHOLDNODE_RED,new ISubscriber(){
			public void Update(EventBase eventArg)
			{				
				persist();
			}
		});
		AddSubscriber(ChessEvent.CHECKRESULT_CLICKWRONGNODE_BLACK,new ISubscriber(){
			public void Update(EventBase eventArg)
			{				
				persist();
			}
		});
		AddSubscriber(ChessEvent.CHECKRESULT_CLICKWRONGNODE_RED,new ISubscriber(){
			public void Update(EventBase eventArg)
			{				
				persist();
			}
		});
		AddSubscriber(ChessEvent.CHECKRESULT_DENYPLAY_BLACK,new ISubscriber(){
			public void Update(EventBase eventArg)
			{				
				persist();
			}
		});
		AddSubscriber(ChessEvent.CHECKRESULT_DENYPLAY_RED,new ISubscriber(){
			public void Update(EventBase eventArg)
			{				
				persist();
			}
		});
		AddSubscriber(ChessEvent.CHECKRESULT_DOGFALL,new ISubscriber(){
			public void Update(EventBase eventArg)
			{				
				persist();
			}
		});
		AddSubscriber(ChessEvent.CHECKRESULT_FIRSTHOLDNODE_BLACK,new ISubscriber(){
			public void Update(EventBase eventArg)
			{				
				persist();
			}
		});
		AddSubscriber(ChessEvent.CHECKRESULT_FIRSTHOLDNODE_RED,new ISubscriber(){
			public void Update(EventBase eventArg)
			{				
				persist();
			}
		});
		AddSubscriber(ChessEvent.CHECKRESULT_HOLDSAMENODE_BLACK,new ISubscriber(){
			public void Update(EventBase eventArg)
			{				
				persist();
			}
		});
		AddSubscriber(ChessEvent.CHECKRESULT_HOLDSAMENODE_RED,new ISubscriber(){
			public void Update(EventBase eventArg)
			{				
				persist();
			}
		});
		AddSubscriber(ChessEvent.CHECKRESULT_NEEDUPDATE,new ISubscriber(){
			public void Update(EventBase eventArg)
			{				
				persist();
			}
		});
		AddSubscriber(ChessEvent.CHECKRESULT_NEEDWAITOPPONENT_BLACK,new ISubscriber(){
			public void Update(EventBase eventArg)
			{				
				persist();
			}
		});
		AddSubscriber(ChessEvent.CHECKRESULT_NEEDWAITOPPONENT_RED,new ISubscriber(){
			public void Update(EventBase eventArg)
			{				
				persist();
			}
		});
		AddSubscriber(ChessEvent.CHECKRESULT_WIN_BLACK,new ISubscriber(){
			public void Update(EventBase eventArg)
			{				
				persist();
			}
		});
		AddSubscriber(ChessEvent.CHECKRESULT_WIN_RED,new ISubscriber(){
			public void Update(EventBase eventArg)
			{				
				persist();
			}
		});
		AddSubscriber(ChessEvent.CHECKRESULT_REDO_BLACK,new ISubscriber(){
			public void Update(EventBase eventArg)
			{				
				persist();
			}
		});
		AddSubscriber(ChessEvent.CHECKRESULT_REDO_RED,new ISubscriber(){
			public void Update(EventBase eventArg)
			{				
				persist();
			}
		});
		AddSubscriber(ChessEvent.CHECKRESULT_UNDO_BLACK,new ISubscriber(){
			public void Update(EventBase eventArg)
			{				
				persist();
			}
		});
		AddSubscriber(ChessEvent.CHECKRESULT_UNDO_RED,new ISubscriber(){
			public void Update(EventBase eventArg)
			{				
				persist();
			}
		});
		AddSubscriber(ChessEvent.CHECKRESULT_TOKILLKING_RED,new ISubscriber(){
			public void Update(EventBase eventArg)
			{				
				persist();
			}
		});
		AddSubscriber(ChessEvent.CHECKRESULT_TOKILLKING_BLACK,new ISubscriber(){
			public void Update(EventBase eventArg)
			{				
				persist();
			}
		});
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException
	{
		out.defaultWriteObject();
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		init();
	}
	
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
	
	private void Play(int from,int to)
	{
		ReDoStack.clear();
		UnDoStack.push(new PlayAction(from,to));
	}
	
	public void UnDo()
	{
		if(!UnDoStack.isEmpty())
		{
			PlayAction undoAction = ReDoStack.push(UnDoStack.pop());
			undoAction.UnDo();	
			String evtName = ChessEvent.CHECKRESULT_UNDO_BLACK;
			if(undoAction.fromType.getIndex()%2==1){
				evtName = ChessEvent.CHECKRESULT_UNDO_RED;
			}
			ChessEvent ev = new ChessEvent(evtName);
			ev.setFromNode(undoAction.from);
			ev.setFromType(undoAction.fromType);
			ev.setToNode(undoAction.to);
			ev.setToType(undoAction.ToType);
			ev.setChessBoardData(ToString());
			Notify(evtName,ev);
		}
	}
	
	public void ReDo()
	{
		if(!ReDoStack.isEmpty())
		{
			PlayAction redoAction = UnDoStack.push(ReDoStack.pop());
			redoAction.ReDo();
			String evtName = ChessEvent.CHECKRESULT_REDO_BLACK;
			if(redoAction.ToType.getIndex()%2==1){
				evtName = ChessEvent.CHECKRESULT_REDO_RED;
			}
			ChessEvent ev = new ChessEvent(evtName);
			ev.setFromNode(redoAction.from);
			ev.setFromType(redoAction.fromType);
			ev.setToNode(redoAction.to);
			ev.setToType(redoAction.ToType);
			ev.setChessBoardData(ToString());
			Notify(evtName,ev);
		}
	}
	
	private Stack<PlayAction> ReDoStack = new Stack<PlayAction>();
	private Stack<PlayAction> UnDoStack = new Stack<PlayAction>();	
	
	
	private class PlayAction implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
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
	
	transient private Map<String,List<ISubscriber>> subscriberList = null;
	
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
		if(subscriberList.containsKey(EventName))
		{
			for(ISubscriber subscr :subscriberList.get(EventName))
			{
				subscr.Update(eventArg);
			}
		}
	}
	
	private void Notify()
	{
		for(ChessEvent ev : rule.getEventsList())
		{
			String eventName = ev.getEventName();
			Notify(eventName,ev);
		}
	}
}
