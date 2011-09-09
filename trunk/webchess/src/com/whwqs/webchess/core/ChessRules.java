package com.whwqs.webchess.core;

public class ChessRules {
	
	private ChessBoard chessBoard;
	
	public static final String CHECKRESULT_NEEDUPDATE = "需要更新棋盘先";
	public static final String CHECKRESULT_NEEDWAITOPPANENT_RED = "红方等待黑方中...";
	public static final String CHECKRESULT_NEEDWAITOPPANENT_BLACK = "黑方等待红方中...";
	public static final String CHECKRESULT_CLICKWRONGNODE_RED = "红方瞎点中...";
	public static final String CHECKRESULT_CLICKWRONGNODE_BLACK = "黑方瞎点中...";
	public static final String CHECKRESULT_FIRSTHOLDNODE_RED ="红方拿棋了";
	public static final String CHECKRESULT_FIRSTHOLDNODE_BLACK ="黑方拿棋了";
	public static final String CHECKRESULT_CHANGEHOLDNODE_RED ="红方换棋了";
	public static final String CHECKRESULT_CHANGEHOLDNODE_BLACK ="黑方换棋了";
	public static final String CHECKRESULT_PLAYOK_RED = "红方已下棋";
	public static final String CHECKRESULT_PLAYOK_BLACK = "黑方已下棋";
	public static final String CHECKRESULT_WIN_RED ="红方胜";
	public static final String CHECKRESULT_WIN_BLACK = "黑方胜";
	public static final String CHECKRESULT_DOGFALL = "建议平局";
	public static final String CHECKRESULT_DENYPLAY_RED = "红方违反规则";
	public static final String CHECKRESULT_DENYPLAY_BLACK = "黑方违反规则";
	
	private Boolean isSuccessMove = false;	
	private Boolean isSuccessHold = false;
	
	private Boolean isGameOver = false;
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
	
	

	public Boolean getIsGameOver() {
		return isGameOver;
	}


	private Rule firstRule ;
	public ChessRules(ChessBoard qp){
		this.chessBoard = qp;
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
	}
	
	private int holdNode = -1;
	private int moveToNode = -1;
	
	public int getHoldNode() {
		return holdNode;
	}
	
	public int getMoveToNode() {
		return moveToNode;
	}	

	public void AcceptClicked(int nodeClicked,Boolean isRedClicked,String clickManCurrentBoard){
		isSuccessMove = false;
		isSuccessHold = false;
		message = "";
		firstRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
	}
	
	private abstract class Rule
	{
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
		
		public abstract void Apply(int nodeClicked,Boolean isRedClicked,String clickManCurrentBoard);
	}
	
	private class CheckIfBoardDataExpired extends Rule
	{

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			if(clickManCurrentBoard!=chessBoard.ToString())
			{
				message = CHECKRESULT_NEEDUPDATE;
				return;
			}
			successorRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
		}
		
	}
	
	private class CheckIfNeedWait extends Rule
	{
		
		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			if((isRedClicked&&NodeType(nodeClicked)==1&&!chessBoard.IsRedToGo())
					||(!isRedClicked&&NodeType(nodeClicked)==2&&chessBoard.IsRedToGo()))
			{				
				if(isRedClicked)
				{
					message = CHECKRESULT_NEEDWAITOPPANENT_RED;
				}
				else
				{
					message = CHECKRESULT_NEEDWAITOPPANENT_BLACK;
				}
				return;
			}
			
			successorRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
			
		}
		
	}	
	
	
	private class CheckIfClickWrongNode extends Rule
	{

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			ChessType clickT = GetNodeType(nodeClicked);
			
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
				return;
			}		
			
			
			successorRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);		
			
		}
		
	}
	
	private class CheckIfFirstHoldNode extends Rule
	{

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			if(holdNode==-1)
			{
				if((isRedClicked&&NodeType(nodeClicked)==1&&chessBoard.IsRedToGo())
						||(!isRedClicked&&NodeType(nodeClicked)==2&&!chessBoard.IsRedToGo()))
				isSuccessHold = true;
				moveToNode = -1;
				if(isRedClicked)
				{
					message = CHECKRESULT_FIRSTHOLDNODE_RED;
				}
				else
				{
					message = CHECKRESULT_FIRSTHOLDNODE_BLACK;
				}
				holdNode = nodeClicked;
				return;
			}
			successorRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
		}
		
	}
	
	private class CheckIfChangeHoldNode extends Rule
	{

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			if(holdNode!=-1)
			{
				if((isRedClicked&&NodeType(nodeClicked)==1&&chessBoard.IsRedToGo())
						||(!isRedClicked&&NodeType(nodeClicked)==2&&!chessBoard.IsRedToGo()))
				isSuccessHold = true;
				moveToNode = -1;
				if(isRedClicked)
				{
					message = CHECKRESULT_CHANGEHOLDNODE_RED;
				}
				else
				{
					message = CHECKRESULT_CHANGEHOLDNODE_BLACK;
				}
				holdNode = nodeClicked;
				return;
			}
			successorRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
		}
		
	}
	
	private class CheckIfIsDogFall extends Rule
	{

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			successorRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
		}
		
	}
	
	private class CheckIfGameOver extends Rule
	{

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			successorRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
		}
		
	}
	
	//不同类型棋子工厂
	private class CoreCheck extends Rule
	{
		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			ChessType fromType = chessBoard.getBoardData()[holdNode/9][holdNode%9];
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
			if(r!=null)
			{
				r.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
			}
			if(isSuccessMove)
			{
				successorRule.Apply(nodeClicked, isRedClicked, clickManCurrentBoard);
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
	
	// 帅
	private class ChessTypeRule0 extends Rule
	{

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			
		}
		
	}	
	// 士
	private class ChessTypeRule1 extends Rule
	{

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			
		}
		
	}
	//相
	private class ChessTypeRule2 extends Rule
	{

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			
		}
		
	}
	//马
	private class ChessTypeRule3 extends Rule
	{

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			
		}
		
	}
	//车
	private class ChessTypeRule4 extends Rule
	{

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			
		}
		
	}
	//炮
	private class ChessTypeRule5 extends Rule
	{

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			
		}
		
	}
	//兵
	private class ChessTypeRule6 extends Rule
	{

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
