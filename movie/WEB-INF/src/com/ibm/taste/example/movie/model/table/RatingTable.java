package com.ibm.taste.example.movie.model.table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.ibm.taste.example.movie.model.Movie;
import com.ibm.taste.example.movie.model.MovieList;
import com.ibm.taste.example.movie.model.Rating;
import com.ibm.taste.example.movie.utils.DBUtil;

public class RatingTable {
	public final static String TABLE_NAME = "movie_preferences";
	public final static String USER_ID_COLUMN = "userID";
	public final static String MOVIE_ID_COLUMN = "movieID";
	public final static String RATING = "preference";
	public final static String TIMESTAMP = "timestamp";

	public static void insertRating(Rating rating) {

	}

	public static void insertRatings(List<Rating> ratings) {
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;
		String sql = "insert into " + TABLE_NAME + " ( " + USER_ID_COLUMN
				+ ", " + MOVIE_ID_COLUMN + ", " + RATING + ", " + TIMESTAMP
				+ ") values (?, ?, ?, ?)";
		try {
			conn.setAutoCommit(false);

			ps = conn.prepareStatement(sql);

			for (Rating rating : ratings) {
				ps.setInt(1, rating.getUser_id());
				ps.setInt(2, rating.getMovie_id());
				ps.setInt(3, rating.getRating());
				ps.setInt(4, rating.getTimestamp());
				ps.addBatch();
			}

			ps.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static MovieList getMoviesByUserID(String userID) {

		String sql = "SELECT * FROM " + TABLE_NAME + " mp, "
				+ MovieTable.TABLE_NAME + " m" + " WHERE " + "mp."
				+ MOVIE_ID_COLUMN + " = m." + MovieTable.ID_COLUMN + " AND "
				+ "mp." + USER_ID_COLUMN + " =  " + userID + " ";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MovieList movies = new MovieList();
		try {
			conn = DBUtil.getConnectionFromDataSource();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			// conn = DBUtil.getConnection();
			// pstmt = conn.prepareStatement(sql);
			// rs = pstmt.executeQuery();
			while (rs.next()) {
				constructMoviesFromResultSet(rs, movies);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return movies;
	}

	private static void constructMoviesFromResultSet(ResultSet rs,
			MovieList movies) {
		try {
			Movie movie = new Movie();
			movie.setId(rs.getInt(MovieTable.ID_COLUMN));
			movie.setName(rs.getString(MovieTable.NAME_COLUMN));
			movie.setYear(rs.getString(MovieTable.PUBLISHED_YEAR_COLUMN));
			String type = rs.getString(MovieTable.TYPE_COLUMN);
			if(type != null){
				movie.setType(Arrays.asList(type.split(", ")));
			}
			Float score = rs.getFloat(RATING);
			movies.add(movie, score);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
