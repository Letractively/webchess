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
	
	private List<ChessEvent> eventsList ;
	public List<ChessEvent> getEventsList()
	{
		return eventsList;
	}
	
	private Boolean isRedGoAhead;
	private Boolean isRedToGo;
	public Boolean getIsRedGoAhead() {
		return isRedGoAhead;
	}

	public void setIsRedGoAhead(Boolean isRedGoAhead) {
		this.isRedGoAhead = isRedGoAhead;
	}

	public Boolean getIsRedToGo() {
		isRedToGo = chessBoard.IsRedToGo();
		return isRedToGo;
	}
	
	private String generateMessage(String eventName){
		return eventName;
	}

	private Boolean isSuccessMove = false;	
	private Boolean isSuccessHold = false;	
	private Boolean isRedWin = false;
	private Boolean isBlackWin = false;
	public Boolean getIsRedWin() {
		return isRedWin;
	}

	public Boolean getIsBlackWin() {
		return isBlackWin;
	}

	private String message = "";
	
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
	
	private Rule firstRule ;
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
			Rule r2 = new CheckIfClickWrongNode();
			Rule r3 = new CheckIfNeedWait();		
			Rule r4 = new CheckIfFirstHoldNode();
			Rule r5 = new CheckIfChangeHoldNode();
			Rule r6 = new CoreCheck();
			Rule r7 = new CheckIfIsDogFall();
			Rule r8 = new CheckIfGameOver();
			
			r1.SetSuccessorRule(r2);
			r2.SetSuccessorRule(r3);
			r3.SetSuccessorRule(r4);
			r4.SetSuccessorRule(r5);
			r5.SetSuccessorRule(r6);
			r6.SetSuccessorRule(r7);
			r7.SetSuccessorRule(r8);
			firstRule = r1;
			eventsList = new ArrayList<ChessEvent>();
	}
	
	private int holdNode = -1;
	private int moveToNode = -1;
	
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
		message = "";
		firstRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
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
			ev.setIsRedToGo(chessBoard.IsRedToGo());
			eventsList.add(ev);
			return ev;
		}
		
		public abstract void Apply(int nodeClicked,Boolean isRedClicked,String clickManCurrentBoard);
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
			
			if((isRedClicked&&NodeType(nodeClicked)==1&&!chessBoard.IsRedToGo())
					||(!isRedClicked&&NodeType(nodeClicked)==2&&chessBoard.IsRedToGo()))
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
			
			if((isRedClicked&&NodeType(nodeClicked)!=1&&!chessBoard.IsRedToGo())
					||(!isRedClicked&&NodeType(nodeClicked)!=2&&chessBoard.IsRedToGo()))
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
			
			if(holdNode==-1)
			{
				if((isRedClicked&&NodeType(nodeClicked)==1&&chessBoard.IsRedToGo())
						||(!isRedClicked&&NodeType(nodeClicked)==2&&!chessBoard.IsRedToGo())){
					isSuccessHold = true;
					holdNode = nodeClicked;
					moveToNode = -1;
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
			}
			if(successorRule!=null)
			{
				successorRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
			}
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
			
			if(holdNode!=-1)
			{
				if((isRedClicked&&NodeType(nodeClicked)==1&&chessBoard.IsRedToGo())
						||(!isRedClicked&&NodeType(nodeClicked)==2&&!chessBoard.IsRedToGo())){
					isSuccessHold = true;
					holdNode = nodeClicked;
					moveToNode = -1;
					if(isRedClicked)
					{
						if(holdNode!=nodeClicked)
						{
							message = generateMessage(ChessEvent.CHECKRESULT_CHANGEHOLDNODE_RED);				
							createEvent(ChessEvent.CHECKRESULT_CHANGEHOLDNODE_RED);
						}
						else
						{
							message = generateMessage(ChessEvent.CHECKRESULT_HOLDSAMENODE_RED);				
							createEvent(ChessEvent.CHECKRESULT_HOLDSAMENODE_RED);
						}
					}
					else
					{
						if(holdNode!=nodeClicked)
						{
							message = generateMessage(ChessEvent.CHECKRESULT_CHANGEHOLDNODE_BLACK);				
							createEvent(ChessEvent.CHECKRESULT_CHANGEHOLDNODE_BLACK);
						}
						else
						{
							message = generateMessage(ChessEvent.CHECKRESULT_HOLDSAMENODE_BLACK);				
							createEvent(ChessEvent.CHECKRESULT_HOLDSAMENODE_BLACK);
						}
					}	
					
					return;
				}
			}
			if(successorRule!=null)
			{
				successorRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
			}
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
			r.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
			if(isSuccessMove)
			{
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
						if(chessBoard.getBoardData()[i][jTemp]!=ChessType.空)
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
		
	}
	
	// 帅
	private class ChessTypeRule0 extends Rule
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			int row = nodeClicked/9;
			int col = nodeClicked%9;
			int fromRow = holdNode/9;
			int fromCol = holdNode%9;
			if(col<3 || col>5)
			{
				return;
			}
			if(row>2 && row<7)
			{
				return;
			}			
			if((Math.abs(row-fromRow)+Math.abs(col-fromCol))==1)
			{
				isSuccessMove=true;
			}
			
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
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			int row = nodeClicked/9;
			int col = nodeClicked%9;
			int fromRow = holdNode/9;
			int fromCol = holdNode%9;
			if(col<3 || col>5)
			{
				return;
			}
			if(row>2 && row<7)
			{
				return;
			}
			if(Math.abs(row-fromRow)==1&&Math.abs(col-fromCol)==1)
			{
				isSuccessMove=true;
			}
			
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
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			int row = nodeClicked/9;
			int col = nodeClicked%9;
			int fromRow = holdNode/9;
			int fromCol = holdNode%9;
			if(isRedClicked&&row>4)
			{
				return;
			}
			if(!isRedClicked&&row<5)
			{
				return;
			}
			if(chessBoard.getBoardData()[(row+fromRow)/2][(col+fromCol)/2]!=ChessType.空){
				return;
			}
			if(Math.abs(row-fromRow)==2&&Math.abs(col-fromCol)==2)
			{
				isSuccessMove=true;
			}
			
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
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			int row = nodeClicked/9;
			int col = nodeClicked%9;
			int fromRow = holdNode/9;
			int fromCol = holdNode%9;
			if(Math.abs(col-fromCol)==2)
			{
				if(Math.abs(row-fromRow)==1
						&&chessBoard.getBoardData()[fromRow][(col+fromCol)/2]==ChessType.空)
				{
					isSuccessMove=true;
				}
			}
			else if(Math.abs(row-fromRow)==2)
			{
				if(Math.abs(col-fromCol)==1
						&&chessBoard.getBoardData()[(row+fromRow)/2][fromCol]==ChessType.空)
				{
					isSuccessMove=true;
				}
			}
			
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
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			int row = nodeClicked/9;
			int col = nodeClicked%9;
			int fromRow = holdNode/9;
			int fromCol = holdNode%9;
			
			if((row!=fromRow)&&(col!=fromCol)){
				return;
			}
			
			if(row==fromRow)
			{
				if(Math.abs(col-fromCol)==1)
				{
					isSuccessMove=true;
				}
				else
				{					
					isSuccessMove=true;
					for(int i=Math.min(col, fromCol)+1;i<Math.max(col, fromCol);i++)
					{
						if(chessBoard.getBoardData()[row][i]!=ChessType.空)
						{
							isSuccessMove = false;
							break;
						}
					}
				}
			}
			else if(col==fromCol)
			{
				if(Math.abs(row-fromRow)==1)
				{
					isSuccessMove=true;
				}
				else
				{					
					isSuccessMove=true;
					for(int i=Math.min(row, fromRow)+1;i<Math.max(row, fromRow);i++)
					{
						if(chessBoard.getBoardData()[i][col]!=ChessType.空)
						{
							isSuccessMove = false;
							break;
						}
					}
				}
			}
			
			
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
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			int row = nodeClicked/9;
			int col = nodeClicked%9;
			int fromRow = holdNode/9;
			int fromCol = holdNode%9;
			
			int n=0;
			
			if((row!=fromRow)&&(col!=fromCol)){
				return;
			}
			
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
					isSuccessMove=true;
				}
			}
			else if(n==1){
				if(chessBoard.getBoardData()[row][col].getIndex()%2	!=chessBoard.getBoardData()[fromRow][fromCol].getIndex()%2
						&&chessBoard.getBoardData()[row][col]!=ChessType.空){
					isSuccessMove=true;
				}
			}
				
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
			int row = nodeClicked/9;
			int col = nodeClicked%9;
			int fromRow = holdNode/9;
			int fromCol = holdNode%9;
				
			if(!((Math.abs(row-fromRow)+Math.abs(col-fromCol))==1))
			{
				return;
			}
			if(isRedClicked)
			{
				if(row<fromRow)
				{
					return;
				}
				if(fromRow<5)
				{
					if(row>fromRow)
					{
						isSuccessMove=true;
					}
				}
				else
				{
					isSuccessMove=true;
				}
			}
			else
			{
				if(row>fromRow)
				{
					return;
				}
				if(fromRow>4)
				{
					if(row<fromRow)
					{
						isSuccessMove=true;
					}
				}
				else
				{
					isSuccessMove=true;
				}
			}
			
		}
		
	}
	
}
