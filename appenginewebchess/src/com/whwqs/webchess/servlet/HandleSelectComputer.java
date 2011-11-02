package com.whwqs.webchess.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class HandleSelectBoard
 */

public class HandleSelectComputer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HandleSelectComputer() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private void ProcessHandle(HttpServletRequest request, HttpServletResponse response) throws Exception
    {    	
    	String num = request.getParameter("room");
    	String type = request.getParameter("type");
    	if(num!=null && type!=null)
    	{
    		request.getSession().setAttribute("room", num);
    		request.getSession().setAttribute("type", type); 
    	}  
    	response.sendRedirect("jsp/computer.jsp");	
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			ProcessHandle(request,response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			ProcessHandle(request,response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
