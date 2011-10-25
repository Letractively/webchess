package com.whwqs.webchess.core;

import java.io.IOException;
import java.util.HashMap;

import org.acerge.engine.*;

import com.whwqs.util.LockManager;

public class ChessComputer {
	
	private static HashMap<String,SearchEngine> EngineHash = new HashMap<String,SearchEngine>();
	
	public static SearchEngine getSearchEngine(String roomNumber) throws IOException {
		if(!EngineHash.containsKey(roomNumber)){
			synchronized (LockManager.GetLock(roomNumber)) {
				if(!EngineHash.containsKey(roomNumber)){
					SearchEngine searchEngine = new SearchEngine();
					searchEngine.setupControl(6, SearchEngine.CLOCK_S * 20,
							SearchEngine.CLOCK_M * 10);
					searchEngine.loadBook("./data/book.txt");
					EngineHash.put(roomNumber, searchEngine);
				}
			}
		}		
		return EngineHash.get(roomNumber);
	}
	
	private final static  HashMap<ChessType, String> BoardHash = new HashMap<ChessType, String>();
	
	//rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR
	static{
		BoardHash.put(ChessType.车, "r");
		BoardHash.put(ChessType.马, "n");
		BoardHash.put(ChessType.相, "b");
		BoardHash.put(ChessType.士, "a");
		BoardHash.put(ChessType.帅, "k");
		BoardHash.put(ChessType.炮, "c");
		BoardHash.put(ChessType.兵, "p");
		
		BoardHash.put(ChessType.車, "R");
		BoardHash.put(ChessType.馬, "N");
		BoardHash.put(ChessType.象, "B");
		BoardHash.put(ChessType.仕, "A");
		BoardHash.put(ChessType.将, "K");
		BoardHash.put(ChessType.砲, "C");
		BoardHash.put(ChessType.卒, "P");
		
		BoardHash.put(ChessType.空, "1");
	}
	
	public static String ConvertToFenString(ChessBoard board){
		String s = "";
		for(int i=0;i<=9;i++){
			int kCount=0;
			for(int j=0;j<=8;j++){
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
			if(i<9){
				s+="/";
			}
		}
		s += " "+(board.IsRedToGo()?"w":"b");
		return s;
	}
	
	public static ActiveBoard Convert(ChessBoard board){
		ActiveBoard ab = new ActiveBoard();
		ab.loadFen(ConvertToFenString(board));
		return ab;
	}
	
	public static MoveNode Compute(ChessBoard board) throws IOException, LostException{
		SearchEngine engine = getSearchEngine(board.getBoardNumber());
		engine.setActiveBoard(Convert(board));
		MoveNode n = engine.getBestMove();
		System.out.println(n.dst);
		System.out.println(n.src);
		return n;
	}
	
	public static void main(String[] args) throws IOException, LostException{
		String s = ChessBoard.GetString(ChessBoard.GetNormalBoardData());
		Compute(new ChessBoard(s));
	}
}
