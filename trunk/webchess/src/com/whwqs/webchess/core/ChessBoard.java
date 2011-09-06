package com.whwqs.webchess.core;

public class ChessBoard {
	private ChessType[][] boardData = new ChessType[][]{
		{ChessType.іµ,ChessType.Вн,ChessType.Па,ChessType.Кї,ChessType.Л§,ChessType.Кї,ChessType.Па,ChessType.Вн,ChessType.іµ},
		{ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ},
		{ChessType.їХ,ChessType.ЕЪ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.ЕЪ,ChessType.їХ},
		{ChessType.±ш,ChessType.їХ,ChessType.±ш,ChessType.їХ,ChessType.±ш,ChessType.їХ,ChessType.±ш,ChessType.їХ,ChessType.±ш},
		{ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ},
		
		{ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ},
		{ChessType.Чд,ChessType.їХ,ChessType.Чд,ChessType.їХ,ChessType.Чд,ChessType.їХ,ChessType.Чд,ChessType.їХ,ChessType.Чд},
		{ChessType.їХ,ChessType.іh,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.іh,ChessType.їХ},
		{ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ,ChessType.їХ},
		{ChessType.Ь‡,ChessType.сR,ChessType.Пу,ChessType.КЛ,ChessType.Ѕ«,ChessType.КЛ,ChessType.Пу,ChessType.сR,ChessType.Ь‡}
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
