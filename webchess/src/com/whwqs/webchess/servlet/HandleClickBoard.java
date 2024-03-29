package com.whwqs.webchess.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acerge.engine.LostException;
import org.acerge.engine.MoveNode;

import com.whwqs.util.LockManager;
import com.whwqs.webchess.ChessBoardManager;
import com.whwqs.webchess.core.*;


@WebServlet("/HandleClickBoard")
public class HandleClickBoard extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HandleClickBoard() {
        super();
        // TODO Auto-generated constructor stub
    }   
    
    private ChessBoard board = null;  
    private Boolean isComputerMove = false;
    
    private String GenerateEventJson(){
    	List<ChessEvent> list = board.GetEventList();
    	String s = "";
    	for(ChessEvent ev:list){
    		ev.setIsComputerMove(isComputerMove);
    		s+=ev.ToJSON()+",";
    	}
    	if(!s.isEmpty()){
    		s = s.substring(0,s.length()-1);
    	}
    	return "["+s+"]";
    }
    
    private String GenerateEventJson(List<ChessEvent> list){    	
    	String s = "";
    	for(ChessEvent ev:list){
    		s+=ev.ToJSON()+",";
    	}
    	if(!s.isEmpty()){
    		s = s.substring(0,s.length()-1);
    	}
    	return "["+s+"]";
    }
    
    private void ProcessHandle(HttpServletRequest request,  HttpServletResponse response) throws IOException, LostException
    {
    	isComputerMove=false;
    	String roomNumber = request.getParameter("room");
    	board =ChessBoardManager.GetChessBoard(roomNumber);
    	String type=request.getParameter("type");
    	String data = "";
    	if(type.equals("click"))
    	{      		
	    	int nodeClicked =Integer.parseInt(request.getParameter("clickNode"));
	    	Boolean isRedClicked = Boolean.valueOf(request.getParameter("isRed"));
	    	String clickManCurrentBoard =  request.getParameter("data");
	    	synchronized(LockManager.GetLock(roomNumber)){
	    		 board.HandleClicked(nodeClicked, isRedClicked, clickManCurrentBoard);
	    	}	
	    	data = GenerateEventJson();
    	}
    	else if(type.equals("timer")){
    		data = GenerateEventJson();
    	}
    	else if(type.equals("changeroom")){
    		ChessEvent ev = new ChessEvent(ChessEvent.CHECKRESULT_NEEDUPDATE);
    		List<ChessEvent> list = board.GetEventList();
    		if(list==null){
    			ev.setChessBoardData(board.ToString());
    			ev.setIsSuccessHold(false);
    			ev.setIsSuccessMove(false);
    		}
    		else{
	    		ChessEvent lastEvent = list.get(list.size()-1);
	    		ev.setFromNode(lastEvent.getFromNode());
	    		ev.setToNode(lastEvent.getToNode());
	    		ev.setChessBoardData(lastEvent.getChessBoardData());
	    		ev.setIsSuccessHold(lastEvent.getIsSuccessHold());
	    		ev.setIsSuccessMove(lastEvent.getIsSuccessMove());	    		
    		}
    		ev.setIsRedToGo(board.IsRedToGo());
    		List<ChessEvent> newList = new ArrayList<ChessEvent>();
    		newList.add(ev);
    		data = GenerateEventJson(newList);
    	}
    	else if(type.equals("undo")){
    		board.UnDo();
    		data = GenerateEventJson();
    	}
    	else if(type.equals("redo")){
    		board.ReDo();
    		data = GenerateEventJson();
    	}
    	else if(type.equals("computer")){
    		String bookPath = request.getSession().getServletContext().getRealPath("data/book.txt");
    		MoveNode node = ChessComputer.Compute(board,bookPath);
    		synchronized(LockManager.GetLock(roomNumber)){
	    		 board.HandleClicked(ChessComputer.EngineCoordinateConvertTo(node.src), board.IsRedToGo(), board.ToString());
	    		 data = GenerateEventJson();
	    		 board = ChessBoardManager.GetChessBoard(roomNumber);
	    		 board.HandleClicked(ChessComputer.EngineCoordinateConvertTo(node.dst), board.IsRedToGo(), board.ToString());	    		 
	    	}
    		isComputerMove = true;
    		data = GenerateEventJson();
    	}
    	
    	try {
			response.getWriter().write("{" +
					"\"type\":\""+type+"\""
					+",\"isRedToGo\":"+board.IsRedToGo()
					+",\"data\":"+data
					+"}");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
    	  	   	
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)  {
		// TODO Auto-generated method stub
		try {
			ProcessHandle(request,response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)  {
		// TODO Auto-generated method stub
		try {
			ProcessHandle(request,response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
