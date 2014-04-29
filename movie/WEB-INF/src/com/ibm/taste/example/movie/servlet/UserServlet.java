package com.ibm.taste.example.movie.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.taste.example.movie.model.User;
import com.ibm.taste.example.movie.model.table.UserTable;

@SuppressWarnings("serial")
public class UserServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {

		String userID = request.getParameter(Parameters.USER_ID);
		String email = request.getParameter(Parameters.EMAIL);
		User user = null;
		if (userID != null) {
			user = UserTable.getUserByID(userID);
		} else if(email != null){
			user = UserTable.getUserByEmail(email);
		} else {
			throw new ServletException("user was not specified");
		}
		if(user != null){
			try {
				writeJSON(response, user);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	private static void writeJSON(HttpServletResponse response, User user) throws IOException {
	    response.setContentType("text/plain");
	    response.setCharacterEncoding("UTF-8");
	    response.setHeader("Cache-Control", "no-cache");
	    PrintWriter writer = response.getWriter();
	    writer.print(user.toJSON());
	  }
}
