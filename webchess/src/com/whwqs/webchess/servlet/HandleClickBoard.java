package com.whwqs.webchess.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.whwqs.util.EventBase;
import com.whwqs.util.ISubscriber;
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
    
    private ChessEvent chessEv = null;
    private ChessBoard board = null;
    
    private void ListenAllBoardEvent(){
    	board.AddSubscriber(ChessEvent.EVENT_BOARDEXPIRE, new ISubscriber(){

			@Override
			public void Update(EventBase eventArg) {
				// TODO Auto-generated method stub
				chessEv = (ChessEvent)eventArg;				
			}
    		
    	});
    	board.AddSubscriber(ChessEvent.EVENT_COMPLETECHECKBOARD, new ISubscriber(){

			@Override
			public void Update(EventBase eventArg) {
				// TODO Auto-generated method stub
				chessEv = (ChessEvent)eventArg;	
			}
    		
    	});
    	board.AddSubscriber(ChessEvent.EVENT_GAME_END, new ISubscriber(){

			@Override
			public void Update(EventBase eventArg) {
				// TODO Auto-generated method stub
				chessEv = (ChessEvent)eventArg;				
			}
    		
    	});
    	board.AddSubscriber(ChessEvent.EVENT_GAME_START, new ISubscriber(){

			@Override
			public void Update(EventBase eventArg) {
				// TODO Auto-generated method stub
				chessEv = (ChessEvent)eventArg;				
			}
    		
    	});
    	board.AddSubscriber(ChessEvent.EVENT_HOLD, new ISubscriber(){

			@Override
			public void Update(EventBase eventArg) {
				// TODO Auto-generated method stub
				chessEv = (ChessEvent)eventArg;				
			}
    		
    	});
    	board.AddSubscriber(ChessEvent.EVENT_PLAY, new ISubscriber(){

			@Override
			public void Update(EventBase eventArg) {
				// TODO Auto-generated method stub
				chessEv = (ChessEvent)eventArg;				
			}
    		
    	});board.AddSubscriber(ChessEvent.EVENT_REDO, new ISubscriber(){

			@Override
			public void Update(EventBase eventArg) {
				// TODO Auto-generated method stub
				chessEv = (ChessEvent)eventArg;				
			}
    		
    	});
    	board.AddSubscriber(ChessEvent.EVENT_TIMEOUT, new ISubscriber(){

			@Override
			public void Update(EventBase eventArg) {
				// TODO Auto-generated method stub
				chessEv = (ChessEvent)eventArg;				
			}
    		
    	});
    	board.AddSubscriber(ChessEvent.EVENT_UNDO, new ISubscriber(){

			@Override
			public void Update(EventBase eventArg) {
				// TODO Auto-generated method stub
				chessEv = (ChessEvent)eventArg;				
			}
    		
    	});
    }
    
    private void ProcessHandle(HttpServletRequest request,  HttpServletResponse response)
    {
    	String roomNumber = request.getParameter("room");
    	board =ChessBoardManager.GetChessBoard(roomNumber);
    	ListenAllBoardEvent();
    	if(request.getParameter("type").equals("click"))
    	{    		
	    	int nodeClicked =Integer.parseInt(request.getParameter("clickNode"));
	    	Boolean isRedClicked = Boolean.valueOf(request.getParameter("isRed"));
	    	String clickManCurrentBoard =  request.getParameter("data");
	    	synchronized(LockManager.GetLock(roomNumber)){
	    		 board.HandleClicked(nodeClicked, isRedClicked, clickManCurrentBoard);
	    	}	    
    	}
    	else if(request.getParameter("type").equals("timer")){
    		 board.HandleClicked(-1, true, "");
    	}
    	if(chessEv!=null){
    		try {
				response.getWriter().write(chessEv.ToJSON());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}	    	
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ProcessHandle(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ProcessHandle(request,response);
	}

}
