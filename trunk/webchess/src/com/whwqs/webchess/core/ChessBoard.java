package com.whwqs.webchess.core;

import java.util.*;

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
		return GetString(boardData);
	}
	
	private String GetString(ChessType[][] board)
	{
		String s = "";
		for(ChessType[] row :board)
		{
			for(ChessType col:row)
			{
				s+=col.getName();
			}
		}
		return s;
	}
	
	public void SetBoard(String data)
	{
		for(int i=0;i<90;i++)
		{
			boardData[i/9][i%9]=ChessType.Get(data.substring(i, i+1));
		}
	}
	
	public void Play(int from,int to)
	{
		
	}
	
	private Stack<PlayAction> ReDoStack = new Stack<PlayAction>();
	private Stack<PlayAction> UnDoStack = new Stack<PlayAction>();
	
	
	private class PlayAction
	{
		private ChessType fromT;
		private ChessType ToT;
		private int from;
		private int to;
		PlayAction(int from,int to)
		{
			this.from = from;
			this.to = to;
			this.fromT = boardData[from/9][from%9];
			this.ToT = boardData[to/9][to%9];
		}
	}
}
