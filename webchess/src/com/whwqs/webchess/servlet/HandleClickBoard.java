package com.whwqs.webchess.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.whwqs.webchess.core.*;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Servlet implementation class HandleClickBoard
 */
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
    
    private void ProcessHandle(HttpServletRequest request, HttpServletResponse response)
    {
    	ChessBoard board = new ChessBoard();
    	FileOutputStream fos = null;
	    ObjectOutputStream out = null;
	    try
	    {
	    	fos = new FileOutputStream("c:/1.txt");
	    	out = new ObjectOutputStream(fos);
	    	out.writeObject(board);
	    	out.close();
		 }
		 catch(IOException ex)
		 {
		     ex.printStackTrace();
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
