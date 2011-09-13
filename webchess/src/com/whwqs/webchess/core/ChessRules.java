package com.whwqs.webchess.core;

import java.io.Serializable;
import java.util.*;

public class ChessRules implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ChessBoard chessBoard;
	
	public static final String CHECKRESULT_NEEDUPDATE = "��Ҫ����������";
	public static final String CHECKRESULT_NEEDWAITOPPONENT_RED = "�췽�ȴ��ڷ���...";
	public static final String CHECKRESULT_NEEDWAITOPPONENT_BLACK = "�ڷ��ȴ��췽��...";
	public static final String CHECKRESULT_CLICKWRONGNODE_RED = "�췽Ϲ����...";
	public static final String CHECKRESULT_CLICKWRONGNODE_BLACK = "�ڷ�Ϲ����...";
	public static final String CHECKRESULT_FIRSTHOLDNODE_RED ="�췽������";
	public static final String CHECKRESULT_FIRSTHOLDNODE_BLACK ="�ڷ�������";
	public static final String CHECKRESULT_CHANGEHOLDNODE_RED ="�췽������";
	public static final String CHECKRESULT_CHANGEHOLDNODE_BLACK ="�ڷ�������";
	public static final String CHECKRESULT_HOLDSAMENODE_RED ="�췽��������";
	public static final String CHECKRESULT_HOLDSAMENODE_BLACK ="�ڷ���������";
	public static final String CHECKRESULT_PLAYOK_RED = "�췽������";
	public static final String CHECKRESULT_PLAYOK_BLACK = "�ڷ�������";
	public static final String CHECKRESULT_WIN_RED ="�췽ʤ";
	public static final String CHECKRESULT_WIN_BLACK = "�ڷ�ʤ";
	public static final String CHECKRESULT_DOGFALL = "����ƽ��";
	public static final String CHECKRESULT_DENYPLAY_RED = "�췽Υ������";
	public static final String CHECKRESULT_DENYPLAY_BLACK = "�ڷ�Υ������";
	
	private List<ChessEvent> eventsList = new ArrayList<ChessEvent>();
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
	
	private void AssembleResponsibilityChain()
	{
		//��װ������
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
	}
	
	private int holdNode = -1;
	private int moveToNode = -1;
	
	public int getHoldNode() {
		return holdNode;
	}
	
	public int getMoveToNode() {
		return moveToNode;
	}	
	
	// ����ʼ�����Ƿ���Ϲ���
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
			if(t==ChessType.��)
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
			if(s!="")
			{
				s = s.replaceAll("|$", "");
			}
			return s;
		}
		
		public abstract void Apply(int nodeClicked,Boolean isRedClicked,String clickManCurrentBoard);
	}
	// ���ʱ���������Ƿ������
	private class CheckIfBoardDataExpired extends Rule
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			
			if(clickManCurrentBoard!=chessBoard.ToString())
			{
				message = CHECKRESULT_NEEDUPDATE;
				ChessEvent ev = new ChessEvent(ChessEvent.EVENT_BOARDEXPIRE);
				ev.setChessBoardData(chessBoard.ToString());
				ev.setMessage(message);
				eventsList.add(ev);
				return;
			}
			if(successorRule!=null)
			{
				successorRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
			}
		}
		
	}
	// �Ƿ�Ҫ�ȴ�����
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
					message = CHECKRESULT_NEEDWAITOPPONENT_RED;					
				}
				else
				{
					message = CHECKRESULT_NEEDWAITOPPONENT_BLACK;
				}
				ChessEvent ev = new ChessEvent(ChessEvent.EVENT_HOLD);
				ev.setFromNode(holdNode);
				ev.setMessage(message);
				eventsList.add(ev);
				return;
			}
			
			if(successorRule!=null)
			{
				successorRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
			}
			
		}
		
	}	
	
	// �Ƿ���Ϲ������
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
					message = CHECKRESULT_CLICKWRONGNODE_RED;
				}
				else
				{
					message = CHECKRESULT_CLICKWRONGNODE_BLACK;
				}
				ChessEvent ev = new ChessEvent(ChessEvent.EVENT_HOLD);
				ev.setFromNode(holdNode);
				ev.setMessage(message);
				eventsList.add(ev);
				return;
			}		
			
			
			if(successorRule!=null)
			{
				successorRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
			}		
			
		}
		
	}
	// �Ƿ�������
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
						||(!isRedClicked&&NodeType(nodeClicked)==2&&!chessBoard.IsRedToGo()))
				isSuccessHold = true;
				holdNode = nodeClicked;
				moveToNode = -1;
				if(isRedClicked)
				{
					message = CHECKRESULT_FIRSTHOLDNODE_RED;
				}
				else
				{
					message = CHECKRESULT_FIRSTHOLDNODE_BLACK;
				}	
				ChessEvent ev = new ChessEvent(ChessEvent.EVENT_HOLD);
				ev.setFromNode(holdNode);
				ev.setMessage(message);
				eventsList.add(ev);
				return;
			}
			if(successorRule!=null)
			{
				successorRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
			}
		}
		
	}
	// �Ƿ��ڻ���
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
						||(!isRedClicked&&NodeType(nodeClicked)==2&&!chessBoard.IsRedToGo()))
				isSuccessHold = true;
				holdNode = nodeClicked;
				moveToNode = -1;
				if(isRedClicked)
				{
					if(holdNode!=nodeClicked)
					{
						message = CHECKRESULT_CHANGEHOLDNODE_RED;
					}
					else
					{
						message = CHECKRESULT_HOLDSAMENODE_RED;
					}
				}
				else
				{
					if(holdNode!=nodeClicked)
					{
						message = CHECKRESULT_CHANGEHOLDNODE_BLACK;
					}
					else
					{
						message = CHECKRESULT_HOLDSAMENODE_BLACK;
					}
				}	
				ChessEvent ev = new ChessEvent(ChessEvent.EVENT_HOLD);
				ev.setFromNode(holdNode);
				ev.setMessage(message);
				eventsList.add(ev);
				return;
			}
			if(successorRule!=null)
			{
				successorRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
			}
		}
		
	}
	
	
	//��ͬ�������ӹ��򹤳�
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
				ChessEvent ev = new ChessEvent(ChessEvent.EVENT_PLAY);
				ev.setFromNode(holdNode);
				ev.setFromType(GetNodeType(holdNode));
				ev.setToNode(moveToNode);
				ev.setToType(GetNodeType(moveToNode));
				ev.setMessage(message);
				eventsList.add(ev);
				if(successorRule!=null)
				{
					successorRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
				}
			}
			else
			{
				if(isRedClicked)
				{
					message = CHECKRESULT_DENYPLAY_RED;
				}
				else
				{
					message = CHECKRESULT_DENYPLAY_BLACK;
				}
			}
		}
		
	}
	
	// ��ǰ�����Ƿ�Ӧ��ƽ��
	private class CheckIfIsDogFall extends Rule
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			//�˹����ܽ���ƽ��
			if(successorRule!=null)
			{
				successorRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
			}
		}
		
	}
	// �ж��Ƿ��ʤ���ߺ�ʤ
	private class CheckIfGameOver extends Rule
	{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			if(GetNodeType(moveToNode)==ChessType.˧)
			{
				isBlackWin = true;				
			}
			else if(GetNodeType(moveToNode)==ChessType.��)
			{
				isRedWin = true;				
			}
			else
			{
				String redKingPosition = GetTypePosition(ChessType.˧,0,2,3,5);
				String blackKingPosition = GetTypePosition(ChessType.��,7,9,3,5);
				String [] redTemp = redKingPosition.split(",");
				String [] blackTemp = blackKingPosition.split(",");
				if(redTemp[1]==blackTemp[1])
				{
					Boolean isKingFaceToFace = true;
					int jTemp = Integer.parseInt(redTemp[1]);
					int i1Temp = Integer.parseInt(redTemp[0]);
					int i2Temp = Integer.parseInt(blackTemp[0]);
					for(int i=i1Temp+1;i<i2Temp;i++)
					{
						if(chessBoard.getBoardData()[i][jTemp]!=ChessType.��)
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
				message = CHECKRESULT_WIN_BLACK;
				ChessEvent ev = new ChessEvent(ChessEvent.EVENT_GAME_END);
				ev.setIsRedWin(false);
				ev.setMessage(message);
				eventsList.add(ev);
				return;
			}
			if(isRedWin)
			{
				message = CHECKRESULT_WIN_RED;
				ChessEvent ev = new ChessEvent(ChessEvent.EVENT_GAME_END);
				ev.setIsRedWin(false);
				ev.setMessage(message);
				eventsList.add(ev);
				return;
			}
			if(successorRule!=null)
			{
				successorRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
			}
		}
		
	}
	
	// ˧
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
	// ʿ
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
	//��
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
			if(Math.abs(row-fromRow)==2&&Math.abs(col-fromCol)==2)
			{
				isSuccessMove=true;
			}
			
		}
		
	}
	//��
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
						&&chessBoard.getBoardData()[row][col/2+fromCol/2]==ChessType.��)
				{
					isSuccessMove=true;
				}
			}
			else if(Math.abs(row-fromRow)==2)
			{
				if(Math.abs(col-fromCol)==1
						&&chessBoard.getBoardData()[row/2+fromRow/2][col]==ChessType.��)
				{
					isSuccessMove=true;
				}
			}
			
		}
		
	}
	//��
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
			if(row==fromRow)
			{
				if(Math.abs(col-fromCol)==1)
				{
					isSuccessMove=true;
				}
				else
				{
					if(col>fromCol)
					{
						col = col^fromCol;
						fromCol = col^fromCol;
						col = col^fromCol;
					}
					isSuccessMove=true;
					for(int i=col+1;i<fromCol;i++)
					{
						if(chessBoard.getBoardData()[row][i]!=ChessType.��)
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
					if(row>fromRow)
					{
						row = row^fromRow;
						fromRow = row^fromRow;
						row = row^fromRow;
					}
					isSuccessMove=true;
					for(int i=row+1;i<fromRow;i++)
					{
						if(chessBoard.getBoardData()[i][col]!=ChessType.��)
						{
							isSuccessMove = false;
							break;
						}
					}
				}
			}
			
			
		}
		
	}
	//��
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
			if(row==fromRow)
			{
				if(Math.abs(col-fromCol)==1)
				{
					return;
				}
				else
				{
					if(col>fromCol)
					{
						col = col^fromCol;
						fromCol = col^fromCol;
						col = col^fromCol;
					}
					int n=0;
					for(int i=col+1;i<fromCol;i++)
					{
						if(chessBoard.getBoardData()[row][i]!=ChessType.��)
						{
							n++;
						}
					}
					if(n==1)
					{
						isSuccessMove=true;
					}
				}
			}
			else if(col==fromCol)
			{
				if(Math.abs(row-fromRow)==1)
				{
					return;
				}
				else
				{
					if(row>fromRow)
					{
						row = row^fromRow;
						fromRow = row^fromRow;
						row = row^fromRow;
					}
					int n=0;
					for(int i=row+1;i<fromRow;i++)
					{
						if(chessBoard.getBoardData()[i][col]!=ChessType.��)
						{
							n++;
						}
					}
					if(n==1)
					{
						isSuccessMove=true;
					}
				}
			}
			
		}
		
	}
	//��
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
