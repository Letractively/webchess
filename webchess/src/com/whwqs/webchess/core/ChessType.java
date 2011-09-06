package com.whwqs.webchess.core;

import java.util.*;

public enum ChessType {	
	空("空",0),
	帅("帅",1),
	士("士",3),
	相("相",5),
	马("马",7),
	车("车",9),
	炮("炮",11),
	兵("兵",13),
	将("将",2),
	仕("仕",4),
	象("象",6),
	馬("馬",8),
	車("車",10),
	砲("砲",12),
	卒("卒",14),
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
