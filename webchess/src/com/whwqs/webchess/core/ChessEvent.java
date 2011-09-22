package com.whwqs.webchess.core;

import java.util.Date;

@SuppressWarnings("serial")
public class ChessEvent extends  com.whwqs.util.EventBase {
	public static final String CHECKRESULT_NEEDUPDATE = "CHECKRESULT_NEEDUPDATE";//需要更新棋盘先";
	public static final String CHECKRESULT_NEEDWAITOPPONENT_RED = "CHECKRESULT_NEEDWAITOPPONENT_RED";//红方等待黑方中...";
	public static final String CHECKRESULT_NEEDWAITOPPONENT_BLACK = "CHECKRESULT_NEEDWAITOPPONENT_BLACK";//黑方等待红方中...";
	public static final String CHECKRESULT_CLICKWRONGNODE_RED = "CHECKRESULT_CLICKWRONGNODE_RED";//红方瞎点中...";
	public static final String CHECKRESULT_CLICKWRONGNODE_BLACK = "CHECKRESULT_CLICKWRONGNODE_BLACK";//黑方瞎点中...";
	public static final String CHECKRESULT_FIRSTHOLDNODE_RED ="CHECKRESULT_FIRSTHOLDNODE_RED";//红方拿棋了";
	public static final String CHECKRESULT_FIRSTHOLDNODE_BLACK ="CHECKRESULT_FIRSTHOLDNODE_BLACK";//黑方拿棋了";
	public static final String CHECKRESULT_CHANGEHOLDNODE_RED ="CHECKRESULT_CHANGEHOLDNODE_RED";//红方换棋了";
	public static final String CHECKRESULT_CHANGEHOLDNODE_BLACK ="CHECKRESULT_CHANGEHOLDNODE_BLACK";//黑方换棋了";
	public static final String CHECKRESULT_HOLDSAMENODE_RED ="CHECKRESULT_HOLDSAMENODE_RED";//红方还在拿棋";
	public static final String CHECKRESULT_HOLDSAMENODE_BLACK ="CHECKRESULT_HOLDSAMENODE_BLACK";//黑方还在拿棋";
	public static final String CHECKRESULT_PLAYOK_RED = "CHECKRESULT_PLAYOK_RED";//红方已下棋";
	public static final String CHECKRESULT_PLAYOK_BLACK = "CHECKRESULT_PLAYOK_BLACK";//黑方已下棋";
	public static final String CHECKRESULT_WIN_RED ="CHECKRESULT_WIN_RED";//红方胜";
	public static final String CHECKRESULT_WIN_BLACK = "CHECKRESULT_WIN_BLACK";//黑方胜";
	public static final String CHECKRESULT_DOGFALL = "CHECKRESULT_DOGFALL";//建议平局";
	public static final String CHECKRESULT_DENYPLAY_RED = "CHECKRESULT_DENYPLAY_RED";//红方违反规则";
	public static final String CHECKRESULT_DENYPLAY_BLACK = "CHECKRESULT_DENYPLAY_BLACK";//黑方违反规则";
	public static final String CHECKRESULT_INIT_CHECK ="CHECKRESULT_INIT_CHECK";//初始棋局检查
	public static final String CHECKRESULT_UNDO_RED = "CHECKRESULT_UNDO_RED";//红方悔棋
	public static final String CHECKRESULT_UNDO_BLACK="CHECKRESULT_UNDO_BLACK";//黑方悔棋
	public static final String CHECKRESULT_REDO_RED="CHECKRESULT_REDO_RED";//红方不悔棋
	public static final String CHECKRESULT_REDO_BLACK="CHECKRESULT_REDO_BLACK";//黑方不悔棋
	public ChessEvent(String EventName)
	{
		super(EventName,new Object());
		this.eventName = EventName;
	}
	private String eventName = "";
	private String wrongNodesPositionData = "";//检查棋盘是否符合规则 格式1|2|3...	
	private String chessBoardData = ""; //cmxsj...
	private Date gameStartAt = new Date();
	private Date gameEndAt = new Date();
	private Boolean isRedWin = false;
	private Boolean isDogfall = false;
	private Date playAt = new Date();
	private Boolean isRedToGo = true;
	private int fromNode = -1;
	private int toNode = -1;
	private ChessType fromType = ChessType.空;
	private ChessType toType = ChessType.空;
	private String message = "";
	public String ToJSON(){
		///*
		  return "{\"eventName\":\""+eventName+"\",\"wrongNodesPositionData\":\""+wrongNodesPositionData		 
				+"\",\"chessBoardData\":\""+chessBoardData+"\",\"gameStartAt\":\""+gameStartAt.toString()
				+"\",\"gameEndAt\":\""+gameEndAt.toString()+"\",\"isRedWin\":\""+isRedWin.toString()
				+"\",\"isDogfall\":\""+isDogfall.toString()+"\",\"playAt\":\""+playAt.toString()
				+"\",\"isRedToGo\":\""+isRedToGo.toString()+"\",\"fromNode\":"+fromNode
				+",\"toNode\":\""+toNode+"\",\"fromType\":\""+fromType.getName()
				+"\",\"toType\":\""+toType.getName()+"\",\"message\":\""+message
				+"\"}";//*/
	}
	public Boolean getIsRedWin() {
		return isRedWin;
	}
	public void setIsRedWin(Boolean isRedWin) {
		this.isRedWin = isRedWin;
	}
	public Boolean getIsDogfall() {
		return isDogfall;
	}
	public void setIsDogfall(Boolean isDogfall) {
		this.isDogfall = isDogfall;
	}	
	public Boolean getIsRedToGo() {
		return isRedToGo;
	}
	public void setIsRedToGo(Boolean isRedToGo) {
		this.isRedToGo = isRedToGo;
	}	
	public ChessType getFromType() {
		return fromType;
	}
	public void setFromType(ChessType fromType) {
		this.fromType = fromType;
	}
	public ChessType getToType() {
		return toType;
	}
	public void setToType(ChessType toType) {
		this.toType = toType;
	}	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getWrongNodesPositionData() {
		return wrongNodesPositionData;
	}
	public void setWrongNodesPositionData(String wrongNodesPositionData) {
		this.wrongNodesPositionData = wrongNodesPositionData;
	}
	public String getChessBoardData() {
		return chessBoardData;
	}
	public void setChessBoardData(String chessBoardData) {
		this.chessBoardData = chessBoardData;
	}
	public Date getGameStartAt() {
		return gameStartAt;
	}
	public void setGameStartAt(Date gameStartAt) {
		this.gameStartAt = gameStartAt;
	}
	public Date getGameEndAt() {
		return gameEndAt;
	}
	public void setGameEndAt(Date gameEndAt) {
		this.gameEndAt = gameEndAt;
	}
	public Date getPlayAt() {
		return playAt;
	}
	public void setPlayAt(Date playAt) {
		this.playAt = playAt;
	}
	public int getFromNode() {
		return fromNode;
	}
	public void setFromNode(int fromNode) {
		this.fromNode = fromNode;
	}
	public int getToNode() {
		return toNode;
	}
	public void setToNode(int toNode) {
		this.toNode = toNode;
	}
	public String getEventName() {
		return eventName;
	}
	
}
