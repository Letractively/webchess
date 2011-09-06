package com.whwqs.webchess.core;

import java.util.*;

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
