package com.ibm.taste.example.movie.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;

import com.ibm.taste.example.movie.model.RecommendMovie;
import com.ibm.taste.example.movie.model.RecommendMovieList;

@SuppressWarnings("serial")
public class MovieRecommenderServlet extends HttpServlet {
	private static final int NUM_TOP_PREFERENCES = 20;
	private static final int DEFAULT_HOW_MANY = 20;

	private Recommender recommender;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		/*
		 * <servlet> 
		 * 		<servlet-name>movie-recommender</servlet-name>
		 * 		<display-name>Movie Recommender</display-name> 
		 * 		<description>Movie recommender servlet</description>
		 * 		<servlet-class>com.ibm.taste.example.movie.servlet.MovieRecommenderServlet</servlet-class> 
		 * 		<init-param>
		 * 			<param-name>recommender-class</param-name>
		 * 			<param-value>com.ibm.taste.example.movie.recommender.MovieRecommender</param-value>
		 * 		</init-param> 
		 * 		<load-on-startup>1</load-on-startup> 
		 * </servlet>
		 */

		String recommenderClassName = config
				.getInitParameter("recommender-class");
		if (recommenderClassName == null) {
			throw new ServletException(
					"Servlet init-param \"recommender-class\" is not defined");
		}
		try {
			MovieRecommenderSingleton.initializeIfNeeded(recommenderClassName);
		} catch (TasteException te) {
			throw new ServletException(te);
		}
		recommender = MovieRecommenderSingleton.getInstance().getRecommender();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {

		String userIDString = request.getParameter(Parameters.USER_ID);
		if (userIDString == null) {
			throw new ServletException("userID was not specified");
		}
		long userID = Long.parseLong(userIDString);
		String howManyString = request.getParameter(Parameters.COUNT);
		int howMany = howManyString == null ? DEFAULT_HOW_MANY : Integer
				.parseInt(howManyString);
		boolean debug = Boolean.parseBoolean(request.getParameter("debug"));
		String format = request.getParameter(Parameters.FORMAT);
		if (format == null) {
			format = "text";
		}

		try {
			List<RecommendedItem> items = recommender
					.recommend(userID, howMany);
			RecommendMovieList movieList = new RecommendMovieList(items);
			if ("text".equals(format)) {
				writePlainText(response, userID, debug, items, movieList);
			} else if ("xml".equals(format)) {
				writeXML(response, items);
			} else if ("json".equals(format)) {
				writeJSON(response, movieList);
			} else {
				throw new ServletException("Bad format parameter: " + format);
			}
		} catch (TasteException te) {
			throw new ServletException(te);
		} catch (IOException ioe) {
			throw new ServletException(ioe);
		}

	}

	private static void writeXML(HttpServletResponse response,
			Iterable<RecommendedItem> items) throws IOException {
		response.setContentType("text/xml");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter writer = response.getWriter();
		writer
				.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?><recommendedItems>");
		for (RecommendedItem recommendedItem : items) {
			writer.print("<item><value>");
			writer.print(recommendedItem.getValue());
			writer.print("</value><id>");
			writer.print(recommendedItem.getItemID());
			writer.print("</id></item>");
		}
		writer.println("</recommendedItems>");
	}

	private static void writeJSON(HttpServletResponse response,
			RecommendMovieList movieList) throws IOException {
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter writer = response.getWriter();
		writer.print(movieList.toJSON());
	}

	private void writePlainText(HttpServletResponse response, long userID,
			boolean debug, Iterable<RecommendedItem> items,
			RecommendMovieList movieList) throws IOException, TasteException {
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter writer = response.getWriter();
		if (debug) {
			writeDebugRecommendations(userID, items, writer);
		} else {
			writeRecommendations(movieList, writer);
		}
	}

	private static void writeRecommendations(RecommendMovieList movieList,
			PrintWriter writer) {
		for (RecommendMovie recommendedItem : movieList.getRecommendMovies()) {
			writer.println(recommendedItem.toString());
		}
	}

	private void writeDebugRecommendations(long userID,
			Iterable<RecommendedItem> items, PrintWriter writer)
			throws TasteException {
		DataModel dataModel = recommender.getDataModel();
		writer.print("User:");
		writer.println(userID);
		writer.print("Recommender: ");
		writer.println(recommender);
		writer.println();
		writer.print("Top ");
		writer.print(NUM_TOP_PREFERENCES);
		writer.println(" Preferences:");
		PreferenceArray rawPrefs = dataModel.getPreferencesFromUser(userID);
		int length = rawPrefs.length();
		PreferenceArray sortedPrefs = rawPrefs.clone();
		sortedPrefs.sortByValueReversed();
		// Cap this at NUM_TOP_PREFERENCES just to be brief
		int max = Math.min(NUM_TOP_PREFERENCES, length);
		for (int i = 0; i < max; i++) {
			Preference pref = sortedPrefs.get(i);
			writer.print(pref.getValue());
			writer.print('\t');
			writer.println(pref.getItemID());
		}
		writer.println();
		writer.println("Recommendations:");
		for (RecommendedItem recommendedItem : items) {
			writer.print(recommendedItem.getValue());
			writer.print('\t');
			writer.println(recommendedItem.getItemID());
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		doGet(request, response);
	}

	@Override
	public String toString() {
		return "RecommenderServlet[recommender:" + recommender + ']';
	}
}
