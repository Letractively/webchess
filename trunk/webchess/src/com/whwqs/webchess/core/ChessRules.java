package com.whwqs.webchess.core;

public class ChessRules {
	
	private ChessBoard chessBoard;
	
	public static final String CHECKRESULT_NEEDUPDATE = "��Ҫ����������";
	public static final String CHECKRESULT_NEEDWAITOPPANENT_RED = "�췽�ȴ��ڷ���...";
	public static final String CHECKRESULT_NEEDWAITOPPANENT_BLACK = "�ڷ��ȴ��췽��...";
	public static final String CHECKRESULT_CLICKWRONGNODE = "��ҪϹ��";
	public static final String CHECKRESULT_FIRSTHOLDNODE_RED ="�췽����";
	public static final String CHECKRESULT_FIRSTHOLDNODE_BLACK ="�ڷ�����";
	public static final String CHECKRESULT_CHANGEHOLDNODE_RED ="�췽����";
	public static final String CHECKRESULT_CHANGEHOLDNODE_BLACK ="�ڷ�����";
	public static final String CHECKRESULT_PLAYOK_RED = "�췽����һ��";
	public static final String CHECKRESULT_PLAYOK_BLACK = "�ڷ�����һ��";
	public static final String CHECKRESULT_WIN_RED ="�췽ʤ";
	public static final String CHECKRESULT_WIN_BLACK = "�ڷ�ʤ";
	public static final String CHECKRESULT_DOGFALL = "����ƽ��";
	public static final String CHECKRESULT_DENYPLAY_RED = "�췽Υ������";
	public static final String CHECKRESULT_DENYPLAY_BLACK = "�ڷ�Υ������";
	
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
		//��װ������
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
	
	//��ͬ�������ӹ���
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
	
	// ˧
	private class ChessTypeRule0 extends Rule
	{

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			
		}
		
	}	
	// ʿ
	private class ChessTypeRule1 extends Rule
	{

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			
		}
		
	}
	//��
	private class ChessTypeRule2 extends Rule
	{

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			
		}
		
	}
	//��
	private class ChessTypeRule3 extends Rule
	{

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			
		}
		
	}
	//��
	private class ChessTypeRule4 extends Rule
	{

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			
		}
		
	}
	//��
	private class ChessTypeRule5 extends Rule
	{

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			
		}
		
	}
	//��
	private class ChessTypeRule6 extends Rule
	{

		@Override
		public void Apply(int nodeClicked, Boolean isRedClicked,
				String clickManCurrentBoard) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
