package com.whwqs.webchess.core;

public class ChessRules {
	
	private ChessBoard chessBoard;
	
	public static final String CHECKRESULT_NEEDUPDATE = "需要更新棋盘先";
	public static final String CHECKRESULT_NEEDWAITOPPANENT_RED = "红方等待黑方中...";
	public static final String CHECKRESULT_NEEDWAITOPPANENT_BLACK = "黑方等待红方中...";
	public static final String CHECKRESULT_CLICKWRONGNODE = "不要瞎点";
	public static final String CHECKRESULT_FIRSTHOLDNODE_RED ="红方拿棋";
	public static final String CHECKRESULT_FIRSTHOLDNODE_BLACK ="黑方拿棋";
	public static final String CHECKRESULT_CHANGEHOLDNODE_RED ="红方换棋";
	public static final String CHECKRESULT_CHANGEHOLDNODE_BLACK ="黑方换棋";
	public static final String CHECKRESULT_PLAYOK_RED = "红方下了一步";
	public static final String CHECKRESULT_PLAYOK_BLACK = "黑方下了一步";
	public static final String CHECKRESULT_WIN_RED ="红方胜";
	public static final String CHECKRESULT_WIN_BLACK = "黑方胜";
	public static final String CHECKRESULT_DOGFALL = "建议平局";
	public static final String CHECKRESULT_DENYPLAY_RED = "红方违反规则";
	public static final String CHECKRESULT_DENYPLAY_BLACK = "黑方违反规则";
	
	private Boolean isSuccessClick = false;	
	private Boolean isReadyPlay = false;
	private Boolean isGameOver = false;
	private String message = "";
	
	public String getMessage(){
		return message;
	}
	
	public Boolean getIsSuccessClick() {
		return isSuccessClick;
	}	
	
	public Boolean getIsReadyPlay()
	{
		return isReadyPlay;
	}
	
	

	public Boolean getIsGameOver() {
		return isGameOver;
	}


	public ChessRules(ChessBoard qp){
		this.chessBoard = qp;
		//组装责任链
	}
	
	private int from = -1;
	private int to = -1;
	
	public int getFrom() {
		return from;
	}
	
	public int getTo() {
		return to;
	}	

	public Boolean AcceptClicked(int nodeClicked,Boolean isRedClicked,String clickManCurrentBoard){
		/*	
		if(chessBoard.IsRedToGo() && !isRedClicked)
		{
			return CHECKRESULT_NEEDWAITOPPANENT;
		}
		
		ChessType clickedType = chessBoard.getBoardData()[nodeClicked/9][nodeClicked%9];
		
		return "";
		//*/
		return true;
	}
	
	private abstract class Rule
	{
		protected Rule successorRule = null;
		public void SetSuccessorRule(Rule r)
		{
			this.successorRule = r;
		}
		public abstract void Apply(int nodeClicked,Boolean isRedClicked,String clickManCurrentBoard);
	}
	
	private class CheckIfNeedWait extends Rule
	{

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class CheckIfBoardDataExpired extends Rule
	{

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class CheckIfClickWrongNode extends Rule
	{

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class CheckIfFirstHoldNode extends Rule
	{

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class CheckIfChangeHoldNode extends Rule
	{

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class CheckIfIsDogFall extends Rule
	{

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class CheckIfGameOver extends Rule
	{

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	//不同类型棋子工厂
	private class CoreCheck extends Rule
	{
		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			ChessType fromType = chessBoard.getBoardData()[from/9][from%9];
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
