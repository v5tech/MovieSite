package com.ibm.taste.example.movie.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.taste.example.movie.model.MovieList;
import com.ibm.taste.example.movie.model.table.RatingTable;

@SuppressWarnings("serial")
public class MovieServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {

		String userID = request.getParameter(Parameters.USER_ID);
		MovieList movies;
		if (userID != null) {
			movies = RatingTable.getMoviesByUserID(userID);
		} else {
			throw new ServletException("user was not specified");
		}
		if (movies != null) {
			try {
				writeJSON(response, movies);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static void writeJSON(HttpServletResponse response, MovieList movies)
			throws IOException {
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter writer = response.getWriter();
		writer.print(movies.toJSON());
	}
}
