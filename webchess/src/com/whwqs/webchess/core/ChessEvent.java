package com.whwqs.webchess.core;

import java.util.Date;

@SuppressWarnings("serial")
public class ChessEvent extends  com.whwqs.util.EventBase {
	public static final String EVENT_COMPLETECHECKBOARD = "���̼�����";
	public static final String EVENT_BOARDEXPIRE = "��鵽����ʱ�������ݹ���";		
	public static final String EVENT_GAME_START = "��ֿ�ʼ";
	public static final String EVENT_HOLD = "����";
	public static final String EVENT_PLAY = "����";
	public static final String EVENT_UNDO = "����";
	public static final String EVENT_REDO = "������";
	public static final String EVENT_END = "��ֽ���";
	public static final String EVENT_TIMEOUT = "���ֳ�ʱ";
	public ChessEvent(String EventName)
	{
		super(EventName,null);
		this.eventName = EventName;
	}
	private String eventName = "";
	private String wrongNodesPositionData = "";//��������Ƿ���Ϲ��� ��ʽ1|2|3...	
	private String chessBoardData = ""; //������ʿ˧...
	private Date gameStartAt;
	private Date gameEndAt;
	private Date playAt;
	private Boolean isRedToGo;
	public Boolean getIsRedToGo() {
		return isRedToGo;
	}
	public void setIsRedToGo(Boolean isRedToGo) {
		this.isRedToGo = isRedToGo;
	}
	private int fromNode = -1;
	private int toNode = -1;
	private String message;
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