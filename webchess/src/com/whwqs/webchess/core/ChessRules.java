package com.whwqs.webchess.core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

public class ChessRules implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ChessBoard chessBoard;
	private Boolean isRedGoAhead;
	private Boolean isRedToGo;
	private Boolean isSuccessMove = false;	
	private Boolean isSuccessHold = false;	
	private Boolean isRedWin = false;
	private Boolean isBlackWin = false;
	private Boolean isRedWillKillKing = false;
	private Boolean isBlackWillKillKing = false;	
	private Boolean isDogfall = false;
	
	private Rule firstRule ;
	private List<ChessEvent> eventsList ;
	private List<ChessEvent> eventsListBak;
	private String message = "";
	private int holdNode = -1;
	private int moveToNode = -1;
	private Rule currentChessRule = null;
	
	public Boolean IsRedToGo(){
		return chessBoard.IsRedToGo();
	}
	
	public List<ChessEvent> getEventsList()
	{
		return eventsListBak;
	}
	
	public Boolean getIsRedGoAhead() {
		return isRedGoAhead;
	}

	public void setIsRedGoAhead(Boolean isRedGoAhead) {
		this.isRedGoAhead = isRedGoAhead;
	}

	public Boolean getIsRedToGo() {
		isRedToGo = IsRedToGo();
		return isRedToGo;
	}
	
	private String generateMessage(String eventName){
		return eventName;
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

	public Boolean getIsRedWin() {
		return isRedWin;
	}

	public Boolean getIsBlackWin() {
		return isBlackWin;
	}
	
	public String getMessage(){
		return message;
	}
	
	public Boolean getIsSuccessMove() {
		return isSuccessMove;
	}	
	
	public Boolean getIsSuccessHold()
	{
		return isSuccessHold;
	}	
	
	public ChessRules(ChessBoard qp){
		chessBoard = qp;
		AssembleResponsibilityChain();
		isRedGoAhead=true;
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException
	{
		out.defaultWriteObject();
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		AssembleResponsibilityChain();
	}
	
	private void AssembleResponsibilityChain()
	{
		//组装责任链
			Rule r1 = new CheckIfBoardDataExpired();
			Rule r2 = new CheckIfNeedWait();		
			Rule r3 = new CheckIfClickWrongNode();			
			Rule r4 = new CheckIfFirstHoldNode();
			Rule r5 = new CheckIfChangeHoldNode();
			Rule r6 = new CoreCheck();
			Rule r7 = new CheckIfIsDogFall();
			Rule r8 = new CheckIfGameOver();
			Rule r9 = new CheckIfWillKillKing();
			
			r1.SetSuccessorRule(r2);
			r2.SetSuccessorRule(r3);
			r3.SetSuccessorRule(r4);
			r4.SetSuccessorRule(r5);
			r5.SetSuccessorRule(r6);
			r6.SetSuccessorRule(r7);
			r7.SetSuccessorRule(r8);
			r8.SetSuccessorRule(r9);
			firstRule = r1;
			eventsList = new ArrayList<ChessEvent>();
	}
	
	public int getHoldNode() {
		return holdNode;
	}
	
	public int getMoveToNode() {
		return moveToNode;
	}	
	
	// 检查初始棋盘是否符合规则
	public String ValidateChessBoardData(ChessType[][] boardData)
	{
		return "";
	}

	public void AcceptClicked(int nodeClicked,Boolean isRedClicked,String clickManCurrentBoard){
		isSuccessMove = false;
		isSuccessHold = false;
		isRedWin = false;
		isBlackWin = false;
		isDogfall=false;
		isBlackWillKillKing=false;
		isRedWillKillKing=false;
		message = "";
		firstRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
		eventsListBak=eventsList;
	}
	
	public Boolean getIsDogfall() {
		return isDogfall;
	}

	public void setIsDogfall(Boolean isDogfall) {
		this.isDogfall = isDogfall;
	}

	private abstract class Rule implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		protected Rule successorRule = null;
		public void SetSuccessorRule(Rule r)
		{
			this.successorRule = r;
		}		
		
		protected ChessType GetNodeType(int i)
		{
			return chessBoard.getBoardData()[i/9][i%9];
		}
		
		protected int NodeType(int i)
		{
			ChessType t = GetNodeType(i);
			if(t==ChessType.空)
			{
				return 0;
			}
			if(t.getIndex()%2==1)
			{
				return 1;
			}
			return 2;
		}
		
		protected String GetTypePosition(ChessType target,int i0,int j0,int i1,int j1)
		{			
			String s = "";
			if(i0>i1)
			{
				i0 = i0^i1;
				i1 = i0^i1;
				i0 = i0^i1;
			}
			if(j0>j1)
			{
				j0 = j0^j1;
				j1 = j0^j1;
				j0 = j0^j1;
			}
			for(int i=i0;i<=i1;i++)
			{
				for(int j=j0;j<=j1;j++)
				{
					if(chessBoard.getBoardData()[i][j]==target)
					{
						s+=i+","+j+"|";
					}
				}
			}
			if(!s.isEmpty())
			{
				s = s.substring(0, s.length()-1);
			}
			return s;
		}
		
		protected ChessEvent createEvent(String eventName){
			ChessEvent ev = new ChessEvent(eventName);
			ev.setIsRedToGo(IsRedToGo());
			ev.setIsSuccessHold(isSuccessHold);
			ev.setIsSuccessMove(isSuccessMove);
			ev.setIsBlackWillKillKing(isBlackWillKillKing);
			ev.setIsRedWillKillKing(isRedWillKillKing);
			ev.setIsRedWin(isRedWin);
			ev.setIsBlackWin(isBlackWin);
			ev.setIsDogfall(isDogfall);
			
			ev.setChessBoardData(chessBoard.ToString());
			ev.setMessage(message);
			ev.setFromNode(holdNode);
			if(holdNode!=-1){
				ev.setFromType(GetNodeType(holdNode));
			}
			ev.setToNode(moveToNode);
			if(moveToNode!=-1){
				ev.setToType(GetNodeType(moveToNode));
			}
			
			String redKingPosition = GetTypePosition(ChessType.帅,0,3,2,5);
			String blackKingPosition = GetTypePosition(ChessType.将,7,3,9,5);
			String[] arr;
			if(!redKingPosition.isEmpty()){
				arr = redKingPosition.split(",");
				ev.setRedKingNode(Integer.valueOf(arr[0])*9+Integer.valueOf(arr[1]));
			}
			if(!blackKingPosition.isEmpty()){
				arr = blackKingPosition.split(",");
				ev.setBlackKingNode(Integer.valueOf(arr[0])*9+Integer.valueOf(arr[1]));
			}			
			eventsList.add(ev);
			return ev;
		}
		
		public abstract void Apply(int nodeClicked,Boolean isRedClicked,String clickManCurrentBoard);
		public abstract String GetAllMoveableNodes(int nodeClicked);
		public abstract Boolean CanMove(int fromRow,int fromCol,int row,int col);
	}
	// 点击时棋盘数据是否过期了
	private class CheckIfBoardDataExpired extends Rule
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			
			if(!clickManCurrentBoard.equals(chessBoard.ToString()))
			{		
				message = generateMessage(ChessEvent.CHECKRESULT_NEEDUPDATE);
				createEvent(ChessEvent.CHECKRESULT_NEEDUPDATE);
				return;
			}
			if(successorRule!=null)
			{
				successorRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
			}
		}

		@Override
		public String GetAllMoveableNodes(int nodeClicked) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Boolean CanMove(int fromRow, int fromCol, int row, int col) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	// 是否要等待对手
	private class CheckIfNeedWait extends Rule
	{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			
			if((isRedClicked&&!IsRedToGo())
					||(!isRedClicked &&IsRedToGo()))
			{				
				if(isRedClicked)
				{
					message = generateMessage(ChessEvent.CHECKRESULT_NEEDWAITOPPONENT_RED);				
					createEvent(ChessEvent.CHECKRESULT_NEEDWAITOPPONENT_RED);		
				}
				else
				{
					message = generateMessage(ChessEvent.CHECKRESULT_NEEDWAITOPPONENT_BLACK);				
					createEvent(ChessEvent.CHECKRESULT_NEEDWAITOPPONENT_BLACK);		
				}
						
				return;
			}
			
			if(successorRule!=null)
			{
				successorRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
			}
			
		}

		@Override
		public String GetAllMoveableNodes(int nodeClicked) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Boolean CanMove(int fromRow, int fromCol, int row, int col) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}	
	
	// 是否在瞎点棋盘
	private class CheckIfClickWrongNode extends Rule
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			
			if(
					(holdNode==-1 || NodeType(holdNode)==0)
					&&
					(
							(isRedClicked&&NodeType(nodeClicked)!=1&&IsRedToGo())
							||
							(!isRedClicked&&NodeType(nodeClicked)!=2&&!IsRedToGo())
					)
				)
			{
				if(isRedClicked)
				{				
					message = generateMessage(ChessEvent.CHECKRESULT_CLICKWRONGNODE_RED);				
					createEvent(ChessEvent.CHECKRESULT_CLICKWRONGNODE_RED);		
				}
				else
				{
					message = generateMessage(ChessEvent.CHECKRESULT_CLICKWRONGNODE_BLACK);				
					createEvent(ChessEvent.CHECKRESULT_CLICKWRONGNODE_BLACK);		
				}			
				
				return;
			}		
			
			
			if(successorRule!=null)
			{
				successorRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
			}		
			
		}

		@Override
		public String GetAllMoveableNodes(int nodeClicked) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Boolean CanMove(int fromRow, int fromCol, int row, int col) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	// 是否在拿棋
	private class CheckIfFirstHoldNode extends Rule
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			
			
			if(holdNode==-1 || NodeType(holdNode)==0){
				isSuccessHold = true;
				isSuccessMove = false;
				holdNode = nodeClicked;				
				if(isRedClicked)
				{
					message = generateMessage(ChessEvent.CHECKRESULT_FIRSTHOLDNODE_RED);				
					createEvent(ChessEvent.CHECKRESULT_FIRSTHOLDNODE_RED);
				}
				else
				{
					message = generateMessage(ChessEvent.CHECKRESULT_FIRSTHOLDNODE_BLACK);			
					createEvent(ChessEvent.CHECKRESULT_FIRSTHOLDNODE_BLACK);
				}	
				
				
				return;
			}
			
			if(successorRule!=null)
			{
				successorRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
			}
		}

		@Override
		public String GetAllMoveableNodes(int nodeClicked) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Boolean CanMove(int fromRow, int fromCol, int row, int col) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	// 是否在换棋
	private class CheckIfChangeHoldNode extends Rule
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			
			if(NodeType(holdNode)!=0&&NodeType(holdNode)==NodeType(nodeClicked)){
				isSuccessHold = true;
				isSuccessMove=false;	
				if(holdNode!=nodeClicked)
				{
					holdNode = nodeClicked;
					if(isRedClicked){
						message = generateMessage(ChessEvent.CHECKRESULT_CHANGEHOLDNODE_RED);				
						createEvent(ChessEvent.CHECKRESULT_CHANGEHOLDNODE_RED);
					}
					else{
						message = generateMessage(ChessEvent.CHECKRESULT_CHANGEHOLDNODE_BLACK);				
						createEvent(ChessEvent.CHECKRESULT_CHANGEHOLDNODE_BLACK);
					}
				}
				else{
					if(isRedClicked){
						message = generateMessage(ChessEvent.CHECKRESULT_HOLDSAMENODE_RED);				
						createEvent(ChessEvent.CHECKRESULT_HOLDSAMENODE_RED);
					}
					else{
						message = generateMessage(ChessEvent.CHECKRESULT_HOLDSAMENODE_BLACK);				
						createEvent(ChessEvent.CHECKRESULT_HOLDSAMENODE_BLACK);
					}
				}
				return;
			}
		
			if(successorRule!=null)
			{
				successorRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
			}
		}

		@Override
		public String GetAllMoveableNodes(int nodeClicked) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Boolean CanMove(int fromRow, int fromCol, int row, int col) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	
	//不同类型棋子规则工厂
	private class CoreCheck extends Rule
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {		
			
			ChessType fromType = GetNodeType(holdNode);
			int n = fromType.getIndex()/3;
			Rule r = null;
			switch(n)
			{
				case 0:
					r=new ChessTypeRule0();
					break;
				case 1:
					r=new ChessTypeRule1();
					break;
				case 2:
					r=new ChessTypeRule2();
					break;
				case 3:
					r=new ChessTypeRule3();
					break;
				case 4:
					r=new ChessTypeRule4();
					break;
				case 5:
					r=new ChessTypeRule5();
					break;
				case 6:
					r=new ChessTypeRule6();
					break;
			}
			currentChessRule= r;
			int row = nodeClicked/9;
			int col = nodeClicked%9;
			int fromRow = holdNode/9;
			int fromCol = holdNode%9;
			if(r.CanMove(fromRow,fromCol,row,col)){
				isSuccessHold=false;
				isSuccessMove = true;			
				moveToNode = nodeClicked;
				if(isRedClicked)
				{
					message = generateMessage(ChessEvent.CHECKRESULT_PLAYOK_RED);				
					createEvent(ChessEvent.CHECKRESULT_PLAYOK_RED);
				}
				else
				{
					message = generateMessage(ChessEvent.CHECKRESULT_PLAYOK_BLACK);				
					createEvent(ChessEvent.CHECKRESULT_PLAYOK_BLACK);
				}
				if(successorRule!=null)
				{
					successorRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
				}
			}
			else
			{
				isSuccessHold=true;
				isSuccessMove = false;
				if(isRedClicked)
				{
					message = generateMessage(ChessEvent.CHECKRESULT_DENYPLAY_RED);				
					createEvent(ChessEvent.CHECKRESULT_DENYPLAY_RED);
				}
				else
				{
					message = generateMessage(ChessEvent.CHECKRESULT_DENYPLAY_BLACK);				
					createEvent(ChessEvent.CHECKRESULT_DENYPLAY_BLACK);
				}
			}
		}

		@Override
		public String GetAllMoveableNodes(int nodeClicked) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Boolean CanMove(int fromRow, int fromCol, int row, int col) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	// 当前局面是否应该平局
	private class CheckIfIsDogFall extends Rule
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			//人工智能建议平局
			if(successorRule!=null)
			{
				successorRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
			}
		}

		@Override
		public String GetAllMoveableNodes(int nodeClicked) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Boolean CanMove(int fromRow, int fromCol, int row, int col) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	//是否将军了
	private class CheckIfWillKillKing extends Rule{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			
			String KingPosition;
			if(isRedClicked)
			{
				KingPosition = GetTypePosition(ChessType.将,7,3,9,5);
			}
			else{
				KingPosition = GetTypePosition(ChessType.帅,0,3,2,5);
			}
			
			String[] arr = KingPosition.split(",");
			int king =  Integer.valueOf(arr[0])*9+Integer.valueOf(arr[1]);
			ChessType oldType = GetNodeType(nodeClicked);
			Move(holdNode,nodeClicked);
			String s=currentChessRule.GetAllMoveableNodes(nodeClicked);
			if(s.indexOf("|"+king+"|")>=0){				
				if(isRedClicked){
					isBlackWillKillKing=false;
					isRedWillKillKing=true;
					message = generateMessage(ChessEvent.CHECKRESULT_TOKILLKING_RED);				
					createEvent(ChessEvent.CHECKRESULT_TOKILLKING_RED);
				}
				else{
					isBlackWillKillKing=true;
					isRedWillKillKing=false;
					message = generateMessage(ChessEvent.CHECKRESULT_TOKILLKING_BLACK);				
					createEvent(ChessEvent.CHECKRESULT_TOKILLKING_BLACK);
				}
			}
			UndoMove(holdNode,nodeClicked,oldType);
		}

		@Override
		public String GetAllMoveableNodes(int nodeClicked) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Boolean CanMove(int fromRow, int fromCol, int row, int col) {
			// TODO Auto-generated method stub
			return null;
		}
		
		private void Move(int from,int to){
			ChessType f = GetNodeType(from);
			chessBoard.getBoardData()[from/9][from%9]=ChessType.空;
			chessBoard.getBoardData()[to/9][to%9]=f;
		}
		
		private void UndoMove(int from,int to,ChessType oldToType){
			ChessType f = GetNodeType(to);
			chessBoard.getBoardData()[from/9][from%9]=f;
			chessBoard.getBoardData()[to/9][to%9]=oldToType;
		}
	}
	
	// 判断是否红胜或者黑胜
	private class CheckIfGameOver extends Rule
	{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			if(GetNodeType(moveToNode)==ChessType.帅)
			{
				isBlackWin = true;				
			}
			else if(GetNodeType(moveToNode)==ChessType.将)
			{
				isRedWin = true;				
			}
			else
			{
				String redKingPosition = GetTypePosition(ChessType.帅,0,3,2,5);
				String blackKingPosition = GetTypePosition(ChessType.将,7,3,9,5);
				String [] redTemp = redKingPosition.split(",");
				String [] blackTemp = blackKingPosition.split(",");
				if(redTemp[1].equals(blackTemp[1]))
				{
					Boolean isKingFaceToFace = true;
					int jTemp = Integer.parseInt(redTemp[1]);
					int i1Temp = Integer.parseInt(redTemp[0]);
					int i2Temp = Integer.parseInt(blackTemp[0]);
					for(int i=i1Temp+1;i<i2Temp;i++)
					{
						if(chessBoard.getBoardData()[i][jTemp]!=ChessType.空&&i!=nodeClicked/9)
						{
							isKingFaceToFace = false;
							break;
						}
					}
					if(isKingFaceToFace)
					{
						if(isRedClicked)
						{
							isBlackWin = true;	
						}
						else
						{
							isRedWin = true;
						}
					}
				}
			}
			if(isBlackWin)
			{
				message = generateMessage(ChessEvent.CHECKRESULT_WIN_BLACK);				
				createEvent(ChessEvent.CHECKRESULT_WIN_BLACK).setIsRedWin(false);
				return;
			}
			if(isRedWin)
			{
				message = generateMessage(ChessEvent.CHECKRESULT_WIN_RED);				
				createEvent(ChessEvent.CHECKRESULT_WIN_RED).setIsRedWin(true);
				return;
			}
			if(successorRule!=null)
			{
				successorRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
			}
		}

		@Override
		public String GetAllMoveableNodes(int nodeClicked) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Boolean CanMove(int fromRow, int fromCol, int row, int col) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	// 帅
	private class ChessTypeRule0 extends Rule
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@Override
		public Boolean CanMove(int fromRow,int fromCol,int row,int col){
			if(fromRow<0 || fromRow>9 || fromCol<0 || fromCol>8 || row<0 || row>9 || col<0 || col >8){
				return false;
			}
			if(NodeType(fromRow*9+fromCol)==NodeType(row*9+col))
			{
				return false;
			}
			if(col<3 || col>5)
			{
				return false;
			}
			if(row>2 && row<7)
			{
				return false;
			}			
			if((Math.abs(row-fromRow)+Math.abs(col-fromCol))==1)
			{
				return true;
			}
			return false;
		}

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			
		}

		@Override
		public String GetAllMoveableNodes(int nodeClicked) {
			// TODO Auto-generated method stub
			if(NodeType(nodeClicked)==0){
				return null;
			}
			ChessType clickType = GetNodeType(nodeClicked);
			if(clickType!= ChessType.将 && clickType!=ChessType.帅){
				return null;
			}
			String s = "";
			int rMin = nodeClicked/9-1;
			int rMax = nodeClicked/9+1;
			int cMin = nodeClicked%9-1;
			int cMax =  nodeClicked%9+1;
			for(int r=rMin;r<=rMax;r++){
				for(int c=cMin;c<=cMax;c++){
					if(CanMove(nodeClicked/9,nodeClicked%9,r,c)){
						s+="|"+(r*9+c);
					}
				}
			}
			if(s.isEmpty()){
				return null;
			}
			return s+"|";
		}
		
	}	
	// 士
	private class ChessTypeRule1 extends Rule
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@Override
		public Boolean CanMove(int fromRow,int fromCol,int row,int col){
			if(fromRow<0 || fromRow>9 || fromCol<0 || fromCol>8 || row<0 || row>9 || col<0 || col >8){
				return false;
			}
			if(NodeType(fromRow*9+fromCol)==NodeType(row*9+col))
			{
				return false;
			}
			if(col<3 || col>5)
			{
				return false;
			}
			if(row>2 && row<7)
			{
				return false;
			}
			if(Math.abs(row-fromRow)==1&&Math.abs(col-fromCol)==1)
			{
				return true;
			}
			return false;
		}

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			
		}

		@Override
		public String GetAllMoveableNodes(int nodeClicked) {
			// TODO Auto-generated method stub
			if(NodeType(nodeClicked)==0){
				return null;
			}
			ChessType clickType = GetNodeType(nodeClicked);
			if(clickType!= ChessType.仕 && clickType!=ChessType.士){
				return null;
			}
			String s = "";
			int rMin = nodeClicked/9-1;
			int rMax = nodeClicked/9+1;
			int cMin = nodeClicked%9-1;
			int cMax =  nodeClicked%9+1;
			for(int r=rMin;r<=rMax;r++){
				for(int c=cMin;c<=cMax;c++){
					if(CanMove(nodeClicked/9,nodeClicked%9,r,c)){
						s+="|"+(r*9+c);
					}
				}
			}
			if(s.isEmpty()){
				return null;
			}
			return s+"|";
		}
		
	}
	//相
	private class ChessTypeRule2 extends Rule
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@Override
		public Boolean CanMove(int fromRow,int fromCol,int row,int col){
			if(fromRow<0 || fromRow>9 || fromCol<0 || fromCol>8 || row<0 || row>9 || col<0 || col >8){
				return false;
			}
			if(NodeType(fromRow*9+fromCol)==NodeType(row*9+col))
			{
				return false;
			}
			Boolean isRedClicked = true;
			if(NodeType(fromRow*9+fromCol)==2){
				isRedClicked = false;
			}
			if(isRedClicked&&row>4)
			{
				return false;
			}
			if(!isRedClicked&&row<5)
			{
				return false;
			}
			if(chessBoard.getBoardData()[(row+fromRow)/2][(col+fromCol)/2]!=ChessType.空){
				return false;
			}
			if(Math.abs(row-fromRow)==2&&Math.abs(col-fromCol)==2)
			{
				return true;
			}
			return true;
		}

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			
			
		}

		@Override
		public String GetAllMoveableNodes(int nodeClicked) {
			// TODO Auto-generated method stub
			if(NodeType(nodeClicked)==0){
				return null;
			}
			ChessType clickType = GetNodeType(nodeClicked);
			if(clickType!= ChessType.相 && clickType!=ChessType.象){
				return null;
			}
			String s = "";
			int rMin = nodeClicked/9-2;
			int rMax = nodeClicked/9+2;
			int cMin = nodeClicked%9-2;
			int cMax =  nodeClicked%9+2;
			for(int r=rMin;r<=rMax;r++){
				for(int c=cMin;c<=cMax;c++){
					if(CanMove(nodeClicked/9,nodeClicked%9,r,c)){
						s+="|"+(r*9+c);
					}
				}
			}
			if(s.isEmpty()){
				return null;
			}
			return s+"|";
		}
		
	}
	//马
	private class ChessTypeRule3 extends Rule
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@Override
		public Boolean CanMove(int fromRow,int fromCol,int row,int col){
			if(fromRow<0 || fromRow>9 || fromCol<0 || fromCol>8 || row<0 || row>9 || col<0 || col >8){
				return false;
			}
			if(NodeType(fromRow*9+fromCol)==NodeType(row*9+col))
			{
				return false;
			}
			if(Math.abs(col-fromCol)==2)
			{
				if(Math.abs(row-fromRow)==1
						&&chessBoard.getBoardData()[fromRow][(col+fromCol)/2]==ChessType.空)
				{
					return true;
				}
			}
			else if(Math.abs(row-fromRow)==2)
			{
				if(Math.abs(col-fromCol)==1
						&&chessBoard.getBoardData()[(row+fromRow)/2][fromCol]==ChessType.空)
				{
					return true;
				}
			}
			return false;
		}

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			
			
		}

		@Override
		public String GetAllMoveableNodes(int nodeClicked) {
			// TODO Auto-generated method stub
			if(NodeType(nodeClicked)==0){
				return null;
			}
			ChessType clickType = GetNodeType(nodeClicked);
			if(clickType!= ChessType.馬 && clickType!=ChessType.马){
				return null;
			}
			String s = "";
			int rMin = nodeClicked/9-2;
			int rMax = nodeClicked/9+2;
			int cMin = nodeClicked%9-2;
			int cMax =  nodeClicked%9+2;
			for(int r=rMin;r<=rMax;r++){
				for(int c=cMin;c<=cMax;c++){
					if(CanMove(nodeClicked/9,nodeClicked%9,r,c)){
						s+="|"+(r*9+c);
					}
				}
			}
			if(s.isEmpty()){
				return null;
			}
			return s+"|";
		}
		
	}
	//车
	private class ChessTypeRule4 extends Rule
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		@Override
		public Boolean CanMove(int fromRow,int fromCol,int row,int col){
			if(fromRow<0 || fromRow>9 || fromCol<0 || fromCol>8 || row<0 || row>9 || col<0 || col >8){
				return false;
			}
			if(NodeType(fromRow*9+fromCol)==NodeType(row*9+col))
			{
				return false;
			}
			if((row!=fromRow)&&(col!=fromCol)){
				return false;
			}
			
			if(row==fromRow)
			{
				if(Math.abs(col-fromCol)==1)
				{
					return true;
				}
				else
				{	
					for(int i=Math.min(col, fromCol)+1;i<Math.max(col, fromCol);i++)
					{
						if(chessBoard.getBoardData()[row][i]!=ChessType.空)
						{
							return false;
						}
					}
				}
			}
			else if(col==fromCol)
			{
				if(Math.abs(row-fromRow)==1)
				{
					return true;
				}
				else
				{	
					for(int i=Math.min(row, fromRow)+1;i<Math.max(row, fromRow);i++)
					{
						if(chessBoard.getBoardData()[i][col]!=ChessType.空)
						{
							return false;
						}
					}
				}
			}
			return true;
		}

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			
			
		}

		@Override
		public String GetAllMoveableNodes(int nodeClicked) {
			// TODO Auto-generated method stub
			if(NodeType(nodeClicked)==0){
				return null;
			}
			ChessType clickType = GetNodeType(nodeClicked);
			if(clickType!= ChessType.车 && clickType!=ChessType.車){
				return null;
			}
			String s = "";
			for(int r=0;r<=9;r++){
				if(CanMove(nodeClicked/9,nodeClicked%9,r,nodeClicked%9)){
					s+="|"+(r*9+nodeClicked%9);
				}
			}
			for(int c=0;c<=8;c++){
				if(CanMove(nodeClicked/9,nodeClicked%9,nodeClicked/9,c)){
					s+="|"+(nodeClicked/9*9+c);
				}
			}			
			if(s.isEmpty()){
				return null;
			}
			return s+"|";
		}
		
	}
	//炮
	private class ChessTypeRule5 extends Rule
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		@Override
		public Boolean CanMove(int fromRow, int fromCol, int row, int col) {
			// TODO Auto-generated method stub
			if(fromRow<0 || fromRow>9 || fromCol<0 || fromCol>8 || row<0 || row>9 || col<0 || col >8){
				return false;
			}
			if(NodeType(fromRow*9+fromCol)==NodeType(row*9+col))
			{
				return false;
			}			
			
			if((row!=fromRow)&&(col!=fromCol)){
				return false;
			}
			int n=0;
			if(row==fromRow)
			{			
				for(int i=Math.min(col, fromCol)+1;i<Math.max(col, fromCol);i++)
				{
					if(chessBoard.getBoardData()[row][i]!=ChessType.空)
					{
						n++;
					}
				}
			}
			else if(col==fromCol)
			{		
				for(int i=Math.min(row, fromRow)+1;i<Math.max(row, fromRow);i++)
				{
					if(chessBoard.getBoardData()[i][col]!=ChessType.空)
					{
						n++;
					}
				}
			}
			if(n==0){
				if(chessBoard.getBoardData()[row][col]==ChessType.空){
					return true;
				}
			}
			else if(n==1){
				if(chessBoard.getBoardData()[row][col].getIndex()%2	!=chessBoard.getBoardData()[fromRow][fromCol].getIndex()%2
						&&chessBoard.getBoardData()[row][col]!=ChessType.空){
					return true;
				}
			}
			return false;
		}

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			
				
		}

		@Override
		public String GetAllMoveableNodes(int nodeClicked) {
			// TODO Auto-generated method stub
			if(NodeType(nodeClicked)==0){
				return null;
			}
			ChessType clickType = GetNodeType(nodeClicked);
			if(clickType!= ChessType.炮 && clickType!=ChessType.砲){
				return null;
			}
			String s = "";
			for(int r=0;r<=9;r++){
				if(CanMove(nodeClicked/9,nodeClicked%9,r,nodeClicked%9)){
					s+="|"+(r*9+nodeClicked%9);
				}
			}
			for(int c=0;c<=8;c++){
				if(CanMove(nodeClicked/9,nodeClicked%9,nodeClicked/9,c)){
					s+="|"+(nodeClicked/9*9+c);
				}
			}			
			if(s.isEmpty()){
				return null;
			}
			return s+"|";
		}
		
	}
	//兵
	private class ChessTypeRule6 extends Rule
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			
		}

		@Override
		public String GetAllMoveableNodes(int nodeClicked) {
			// TODO Auto-generated method stub
			if(NodeType(nodeClicked)==0){
				return null;
			}
			ChessType clickType = GetNodeType(nodeClicked);
			if(clickType!= ChessType.兵 && clickType!=ChessType.卒){
				return null;
			}
			String s = "";
			int rMin = nodeClicked/9-1;
			int rMax = nodeClicked/9+1;
			int cMin = nodeClicked%9-1;
			int cMax =  nodeClicked%9+1;
			for(int r=rMin;r<=rMax;r++){
				for(int c=cMin;c<=cMax;c++){
					if(CanMove(nodeClicked/9,nodeClicked%9,r,c)){
						s+="|"+(r*9+c);
					}
				}
			}
			if(s.isEmpty()){
				return null;
			}
			return s+"|";
		}

		@Override
		public Boolean CanMove(int fromRow, int fromCol, int row, int col) {
			// TODO Auto-generated method stub
			if(fromRow<0 || fromRow>9 || fromCol<0 || fromCol>8 || row<0 || row>9 || col<0 || col >8){
				return false;
			}
			if(NodeType(fromRow*9+fromCol)==NodeType(row*9+col))
			{
				return false;
			}		
			
			if(!((Math.abs(row-fromRow)+Math.abs(col-fromCol))==1))
			{
				return false;
			}
			Boolean isRedClicked = true;
			if(NodeType(fromRow*9+fromCol)==2){
				isRedClicked = false;
			}
			if(isRedClicked)
			{
				if(row<fromRow)
				{
					return false;
				}
				if(fromRow<5)
				{
					if(row>fromRow)
					{
						return true;
					}
				}
				else
				{
					return true;
				}
			}
			else
			{
				if(row>fromRow)
				{
					return false;
				}
				if(fromRow>4)
				{
					if(row<fromRow)
					{
						return true;
					}
				}
				else
				{
					return true;
				}
			}
			return false;
		}		
	}
	
}
