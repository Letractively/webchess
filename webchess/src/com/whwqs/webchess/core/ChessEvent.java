package com.whwqs.webchess.core;

import java.util.Date;

@SuppressWarnings("serial")
public class ChessEvent extends  com.whwqs.util.EventBase {
	public static final String EVENT_COMPLETECHECKBOARD = "EVENT_COMPLETECHECKBOARD";//棋盘检查完成";
	public static final String EVENT_BOARDEXPIRE = "EVENT_BOARDEXPIRE";//检查到操作时棋盘数据过期";		
	public static final String EVENT_GAME_START = "EVENT_GAME_START";//棋局开始";
	public static final String EVENT_HOLD = "EVENT_HOLD";//拿棋";
	public static final String EVENT_PLAY = "EVENT_PLAY";//落子";
	public static final String EVENT_UNDO = "EVENT_UNDO";//悔棋";
	public static final String EVENT_REDO = "EVENT_REDO";//不悔了";
	public static final String EVENT_GAME_END = "EVENT_GAME_END";//棋局结束";
	public static final String EVENT_TIMEOUT = "EVENT_TIMEOUT";//棋手超时";
	public ChessEvent(String EventName)
	{
		super(EventName,null);
		this.eventName = EventName;
	}
	private String eventName = "";
	private String wrongNodesPositionData = "";//检查棋盘是否符合规则 格式1|2|3...	
	private String chessBoardData = ""; //cmxsj...
	private Date gameStartAt;
	private Date gameEndAt;
	private Boolean isRedWin;
	private Boolean isDogfall;
	private Date playAt;
	private Boolean isRedToGo;
	private int fromNode = -1;
	private int toNode = -1;
	private ChessType fromType;
	private ChessType toType;
	private String message;
	public String ToJSON(){
		return "{\"eventName\":\""+eventName+"\",\"wrongNodesPositionData\":\""+wrongNodesPositionData
				+"\",\"chessBoardData\":\""+chessBoardData+"\",\"gameStartAt\":\""+gameStartAt.toString()
				+"\",\"gameEndAt\":\""+gameEndAt.toString()+"\",\"isRedWin\":\""+isRedWin.toString()
				+"\",\"isDogfall\":\""+isDogfall.toString()+"\",\"playAt\":\""+playAt.toString()
				+"\",\"isRedToGo\":\""+isRedToGo.toString()+"\",\"fromNode\":"+fromNode
				+",\"toNode\":\""+toNode+"\",\"fromType\":\""+fromType.getName()
				+"\",\"toType\":\""+toType.getName()+"\",\"message\":\""+message
				+"\"}";
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
