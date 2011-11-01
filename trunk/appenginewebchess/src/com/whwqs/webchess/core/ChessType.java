package com.whwqs.webchess.core;

import java.util.*;

public enum ChessType {	
	��("k",0),
	˧("j",1),
	ʿ("s",3),
	��("x",7),
	��("m",9),
	��("c",13),
	��("p",15),
	��("b",19),
	��("J",2),
	��("S",4),
	��("X",6),
	�R("M",10),
	܇("C",12),
	�h("P",16),
	��("B",18),
	;
	private int v;
	private String s;
	ChessType(String s,int v)
	{
		this.v = v;
		this.s = s;
	}
	public int getIndex()
	{
		return v;
	}
	public String getName()
	{
		return s;
	}
	private static final Map<String,ChessType> map = new HashMap<String,ChessType>();
	
	static{
		for(ChessType item:EnumSet.allOf(ChessType.class))
		{
			map.put(item.getName(), item);
		}
	}
	
	public static ChessType Get(String name)
	{
		return map.get(name);
	}
}
