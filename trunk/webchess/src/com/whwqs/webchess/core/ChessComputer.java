package com.whwqs.webchess.core;

import java.util.HashMap;

import org.acerge.engine.*;

public class ChessComputer {
	private static SearchEngine searchEngine;
	public static SearchEngine getSearchEngine() {
		if (searchEngine == null) {
			searchEngine = new SearchEngine();
			searchEngine.setupControl(6, SearchEngine.CLOCK_S * 20,
					SearchEngine.CLOCK_M * 10);

		}
		return searchEngine;
	}
	
	public final static  HashMap<ChessType, String> BoardHash = new HashMap<ChessType, String>(){

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;};
	
	public static String ConvertToFenString(ChessBoard board){
		String s = "";
		for(int i=0;i<=9;i++){
			for(int j=0;j<=8;j++){
				
			}
		}
	}
	
	public static ActiveBoard Convert(ChessBoard board){
		String boardStr = board.ToString();
	}
}
