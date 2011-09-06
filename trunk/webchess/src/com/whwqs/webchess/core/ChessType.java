package com.whwqs.webchess.core;

import java.util.*;

public enum ChessType {	
	��("��",0),
	˧("˧",1),
	ʿ("ʿ",3),
	��("��",5),
	��("��",7),
	��("��",9),
	��("��",11),
	��("��",13),
	��("��",2),
	��("��",4),
	��("��",6),
	�R("�R",8),
	܇("܇",10),
	�h("�h",12),
	��("��",14),
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
