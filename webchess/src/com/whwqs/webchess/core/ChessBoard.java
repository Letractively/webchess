package com.whwqs.webchess.core;

public class ChessBoard {
	private ChessType[][] boardData = new ChessType[][]{
		{ChessType.��,ChessType.��,ChessType.��,ChessType.ʿ,ChessType.˧,ChessType.ʿ,ChessType.��,ChessType.��,ChessType.��},
		{ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��},
		{ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��},
		{ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��},
		{ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��},
		
		{ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��},
		{ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��},
		{ChessType.��,ChessType.�h,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.�h,ChessType.��},
		{ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��},
		{ChessType.܇,ChessType.�R,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.��,ChessType.�R,ChessType.܇}
	};

	public ChessType[][] getBoardData() {
		return boardData;
	}
	
	public String ToString()
	{
		String s = "";
		for(ChessType[] row :boardData)
		{
			for(ChessType col:row)
			{
				s+=col.GetS();
			}
		}
		return s;
	}
	
	public void InitBoard(String data)
	{
		for(int i=0;i<90;i++)
		{
			boardData[i/9][i%10]=ChessType.Get(data.substring(i, i));
		}
	}
}
