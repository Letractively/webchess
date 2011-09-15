package com.whwqs.webchess.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class HandleSelectBoard
 */
@WebServlet("/HandleSelectBoard")
public class HandleSelectBoard extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HandleSelectBoard() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private void ProcessHandle(HttpServletRequest request, HttpServletResponse response)
    {    	
    	String num = request.getParameter("number");
    	String isRed = request.getParameter("isRed");
    	if(num!=null && isRed!=null)
    	{
    		request.getSession().setAttribute("number", num);
    		request.getSession().setAttribute("isRed", isRed);
    		try {
				response.getWriter().write("1");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	else
    	{
    		try {
				response.getWriter().write("0");
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
