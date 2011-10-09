package com.whwqs.webchess.core;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class ChessEvent extends  com.whwqs.util.EventBase implements Serializable   {
	public static final String CHECKRESULT_NEEDUPDATE = "CHECKRESULT_NEEDUPDATE";//��Ҫ����������";
	public static final String CHECKRESULT_NEEDWAITOPPONENT_RED = "CHECKRESULT_NEEDWAITOPPONENT_RED";//�췽�ȴ��ڷ���...";
	public static final String CHECKRESULT_NEEDWAITOPPONENT_BLACK = "CHECKRESULT_NEEDWAITOPPONENT_BLACK";//�ڷ��ȴ��췽��...";
	public static final String CHECKRESULT_CLICKWRONGNODE_RED = "CHECKRESULT_CLICKWRONGNODE_RED";//�췽Ϲ����...";
	public static final String CHECKRESULT_CLICKWRONGNODE_BLACK = "CHECKRESULT_CLICKWRONGNODE_BLACK";//�ڷ�Ϲ����...";
	public static final String CHECKRESULT_FIRSTHOLDNODE_RED ="CHECKRESULT_FIRSTHOLDNODE_RED";//�췽������";
	public static final String CHECKRESULT_FIRSTHOLDNODE_BLACK ="CHECKRESULT_FIRSTHOLDNODE_BLACK";//�ڷ�������";
	public static final String CHECKRESULT_CHANGEHOLDNODE_RED ="CHECKRESULT_CHANGEHOLDNODE_RED";//�췽������";
	public static final String CHECKRESULT_CHANGEHOLDNODE_BLACK ="CHECKRESULT_CHANGEHOLDNODE_BLACK";//�ڷ�������";
	public static final String CHECKRESULT_HOLDSAMENODE_RED ="CHECKRESULT_HOLDSAMENODE_RED";//�췽��������";
	public static final String CHECKRESULT_HOLDSAMENODE_BLACK ="CHECKRESULT_HOLDSAMENODE_BLACK";//�ڷ���������";
	public static final String CHECKRESULT_PLAYOK_RED = "CHECKRESULT_PLAYOK_RED";//�췽������";
	public static final String CHECKRESULT_PLAYOK_BLACK = "CHECKRESULT_PLAYOK_BLACK";//�ڷ�������";
	public static final String CHECKRESULT_WIN_RED ="CHECKRESULT_WIN_RED";//�췽ʤ";
	public static final String CHECKRESULT_WIN_BLACK = "CHECKRESULT_WIN_BLACK";//�ڷ�ʤ";
	public static final String CHECKRESULT_DOGFALL = "CHECKRESULT_DOGFALL";//����ƽ��";
	public static final String CHECKRESULT_DENYPLAY_RED = "CHECKRESULT_DENYPLAY_RED";//�췽Υ������";
	public static final String CHECKRESULT_DENYPLAY_BLACK = "CHECKRESULT_DENYPLAY_BLACK";//�ڷ�Υ������";
	public static final String CHECKRESULT_INIT_CHECK ="CHECKRESULT_INIT_CHECK";//��ʼ��ּ��
	public static final String CHECKRESULT_UNDO_RED = "CHECKRESULT_UNDO_RED";//�췽����
	public static final String CHECKRESULT_UNDO_BLACK="CHECKRESULT_UNDO_BLACK";//�ڷ�����
	public static final String CHECKRESULT_REDO_RED="CHECKRESULT_REDO_RED";//�췽������
	public static final String CHECKRESULT_REDO_BLACK="CHECKRESULT_REDO_BLACK";//�ڷ�������
	public static final String CHECKRESULT_TOKILLKING_RED = "CHECKRESULT_TOKILLKING_RED";//�췽��
	public static final String CHECKRESULT_TOKILLKING_BLACK = "CHECKRESULT_TOKILLKING_BLACK";//�ڷ���
	public ChessEvent(String EventName)
	{
		super(EventName,new Object());
		this.eventName = EventName;
	}
	private String eventName = "";
	private String wrongNodesPositionData = "";//��������Ƿ���Ϲ��� ��ʽ1|2|3...	
	private String chessBoardData = ""; //cmxsj...
	private Date gameStartAt = new Date();
	private Date gameEndAt = new Date();	
	private Date playAt = new Date();	
	private int fromNode = -1;
	private int toNode = -1;
	private int redKingNode = -1;
	private int blackKingNode = -1;
	private ChessType fromType = ChessType.��;
	private ChessType toType = ChessType.��;
	private String message = "";
	
	private Boolean isRedToGo = true;
	private Boolean isRedWin = false;
	private Boolean isBlackWin = false;
	private Boolean isDogfall = false;
	private Boolean isRedWillKillKing = false;
	private Boolean isBlackWillKillKing = false;
	private Boolean isSuccessMove = false;	
	private Boolean isSuccessHold = false;	
	public String ToJSON(){
		///*
		  return "{\"eventName\":\""+eventName+"\",\"wrongNodesPositionData\":\""+wrongNodesPositionData		 
				+"\",\"chessBoardData\":\""+chessBoardData+"\",\"gameStartAt\":\""+gameStartAt.toString()
				+"\",\"gameEndAt\":\""+gameEndAt.toString()+"\",\"isRedWin\":\""+isRedWin.toString()
				+"\",\"isBlackWin\":\""+isBlackWin.toString()
				+"\",\"isDogfall\":\""+isDogfall.toString()+"\",\"playAt\":\""+playAt.toString()
				+"\",\"isRedToGo\":\""+isRedToGo.toString()+"\",\"fromNode\":"+fromNode
				+",\"toNode\":\""+toNode+"\",\"fromType\":\""+fromType.getName()
				+"\",\"redKingNode\":"+redKingNode+",\"blackKingNode\":\""+blackKingNode
				+"\",\"isRedWillKillKing\":\""+isRedWillKillKing.toString()
				+"\",\"isBlackWillKillKing\":\""+isBlackWillKillKing.toString()
				+"\",\"isSuccessMove\":\""+isSuccessMove.toString()
				+"\",\"isSuccessHold\":\""+isSuccessHold.toString()
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
	public int getRedKingNode() {
		return redKingNode;
	}
	public void setRedKingNode(int redKingNode) {
		this.redKingNode = redKingNode;
	}
	public int getBlackKingNode() {
		return blackKingNode;
	}
	public void setBlackKingNode(int blackKingNode) {
		this.blackKingNode = blackKingNode;
	}
	public Boolean getIsRedWillKillKing() {
		return isRedWillKillKing;
	}
	public void setIsRedWillKillKing(Boolean isRedWillKillKing) {
		this.isRedWillKillKing = isRedWillKillKing;
	}
	public Boolean getIsBlackWillKillKing() {
		return isBlackWillKillKing;
	}
	public void setIsBlackWillKillKing(Boolean isBlackWillKillKing) {
		this.isBlackWillKillKing = isBlackWillKillKing;
	}
	public Boolean getIsSuccessMove() {
		return isSuccessMove;
	}
	public void setIsSuccessMove(Boolean isSuccessMove) {
		this.isSuccessMove = isSuccessMove;
	}
	public Boolean getIsSuccessHold() {
		return isSuccessHold;
	}
	public void setIsSuccessHold(Boolean isSuccessHold) {
		this.isSuccessHold = isSuccessHold;
	}
	public Boolean getIsBlackWin() {
		return isBlackWin;
	}
	public void setIsBlackWin(Boolean isBlackWin) {
		this.isBlackWin = isBlackWin;
	}
	
}