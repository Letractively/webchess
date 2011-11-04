package com.whwqs.webchess.core;

import java.util.*;

public enum ChessType {	
	空("k",0),
	帅("j",1),
	士("s",3),
	相("x",7),
	马("m",9),
	车("c",13),
	炮("p",15),
	兵("b",19),
	将("J",2),
	仕("S",4),
	象("X",6),
	馬("M",10),
	車("C",12),
	砲("P",16),
	卒("B",18),
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
