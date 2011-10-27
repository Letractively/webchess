package com.whwqs.webchess.core;

import java.io.IOException;
import java.util.HashMap;

import org.acerge.engine.*;

import com.whwqs.util.LockManager;
import com.whwqs.webchess.ChessBoardManager;

public class ChessComputer {
	
	private static HashMap<String,SearchEngine> EngineHash = new HashMap<String,SearchEngine>();
	
	public static SearchEngine getSearchEngine(String roomNumber,String bookPath) throws IOException {
		SearchEngine searchEngine;
		if(!EngineHash.containsKey(roomNumber)){
			synchronized (LockManager.GetLock(roomNumber)) {
				if(!EngineHash.containsKey(roomNumber)){
					searchEngine = new SearchEngine();
					searchEngine.setupControl(6, SearchEngine.CLOCK_S * 20,
							SearchEngine.CLOCK_M * 10);
					searchEngine.loadBook(bookPath);
					EngineHash.put(roomNumber, searchEngine);
				}
			}
		}		
		searchEngine = EngineHash.get(roomNumber);
		searchEngine.clearHash();
		searchEngine.clearHistTab();
		return searchEngine;
	}
	
	private final static  HashMap<ChessType, String> BoardHash = new HashMap<ChessType, String>();
	
	//rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR
	//rnbakabnr/9/4c2c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR b
	static{
		BoardHash.put(ChessType.车, "R");
		BoardHash.put(ChessType.马, "N");
		BoardHash.put(ChessType.相, "B");
		BoardHash.put(ChessType.士, "A");
		BoardHash.put(ChessType.帅, "K");
		BoardHash.put(ChessType.炮, "C");
		BoardHash.put(ChessType.兵, "P");
		
		BoardHash.put(ChessType.車, "r");
		BoardHash.put(ChessType.馬, "n");
		BoardHash.put(ChessType.象, "b");
		BoardHash.put(ChessType.仕, "a");
		BoardHash.put(ChessType.将, "k");
		BoardHash.put(ChessType.砲, "c");
		BoardHash.put(ChessType.卒, "p");
		
		BoardHash.put(ChessType.空, "1");
	}
	
	public static String ConvertToFenString(ChessBoard board){
		String s = "";
		for(int i=9;i>=0;i--){
			int kCount=0;
			for(int j=8;j>=0;j--){
				String v = BoardHash.get(board.getBoardData()[i][j]);
				if(!v.equals("1")){
					if(kCount>0){
						s+=String.valueOf(kCount);
						kCount = 0;
					}
					s+=v;
				}
				else{
					kCount++;
				}
			}
			if(kCount>0){
				s+=String.valueOf(kCount);
			}
			if(i>0){
				s+="/";
			}
		}
		s += " "+(board.IsRedToGo()?"w":"b");
		return s;
	}
	
	public static ActiveBoard Convert(ChessBoard board){
		ActiveBoard ab = new ActiveBoard();
		String s = ConvertToFenString(board);
		ab.loadFen(s);
		//System.out.println(s);
		return ab;
	}
	
	public static MoveNode Compute(ChessBoard board,String bookPath) throws IOException, LostException{
		SearchEngine engine = getSearchEngine(board.getBoardNumber(),bookPath);
		engine.setActiveBoard(Convert(board));
		MoveNode n = engine.getBestMove();
		//System.out.println("from: "+n.src);
		//System.out.println("to: "+n.dst);
		return n;
	}
	
	public static MoveNode Compute(String computerNumber,String bookPath) throws IOException, LostException{
		ChessBoard board = ChessBoardManager.GetChessBoard(computerNumber);
		return Compute(board,bookPath);
	}
	
	public static void main(String[] args) throws IOException, LostException{
				Compute("1","./data/book.txt");
	}
	
	public static int EngineCoordinateConvertTo(int engineCoor){
		int x = engineCoor/10;
		int y = engineCoor%10;
		return y*9+8-x;
	}
	
	public static int CoordinateConvertToEngine(int chessCoor){
		int x = chessCoor/9;
		int y = chessCoor%9;
		return (8-y)*10+x;
	}
}
