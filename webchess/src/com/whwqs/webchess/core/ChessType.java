package com.whwqs.webchess.core;

import java.util.*;

public enum ChessType {	
	��("��",0),
	˧("˧",1),
	ʿ("ʿ",3),
	��("��",7),
	��("��",9),
	��("��",13),
	��("��",15),
	��("��",19),
	��("��",2),
	��("��",4),
	��("��",6),
	�R("�R",10),
	܇("܇",12),
	�h("�h",16),
	��("��",18),
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
