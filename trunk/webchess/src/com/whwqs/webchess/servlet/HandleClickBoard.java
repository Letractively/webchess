package com.whwqs.webchess.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    
    private String GenerateEventJson(){
    	List<ChessEvent> list = board.GetEventList();
    	String s = "";
    	for(ChessEvent ev:list){
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
    
    private void ProcessHandle(HttpServletRequest request,  HttpServletResponse response)
    {
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
    		List<ChessEvent> newList = new ArrayList<ChessEvent>();
    		newList.add(ev);
    		data = GenerateEventJson(newList);
    	}
    	
    	try {
			response.getWriter().write("{\"type\":\""+type+"\",\"data\":"+data+"}");
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
		ProcessHandle(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)  {
		// TODO Auto-generated method stub
		ProcessHandle(request,response);
	}

}
