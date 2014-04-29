package com.ibm.taste.example.movie.model.table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.taste.example.movie.model.Movie;
import com.ibm.taste.example.movie.utils.DBUtil;
import com.ibm.taste.example.movie.utils.StringUtil;

public class MovieTable{
	public final static String TABLE_NAME = "movies";
	public final static String ID_COLUMN = "id";
	public final static String NAME_COLUMN = "name";
	public final static String PUBLISHED_YEAR_COLUMN = "published_year";
	public final static String TYPE_COLUMN = "type";
	
	public static void insertMovie(Movie movie){
		
	}
	
	public static void insertMovies(List<Movie> movies){
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;
		String sql = "insert into "
				+ TABLE_NAME + " ( "
				+ ID_COLUMN + ", "
				+ NAME_COLUMN + ", "
				+ PUBLISHED_YEAR_COLUMN + ", "
				+ TYPE_COLUMN 
				+ ") values (?, ?, ?, ?)";
		try {
			conn.setAutoCommit(false);

			ps = conn.prepareStatement(sql);

			for (Movie movie : movies) {
				ps.setInt(1, movie.getId());
				ps.setString(2, movie.getName());
				ps.setString(3, movie.getYear());
				ps.setString(4, StringUtil.connectString(movie.getType(), ", "));
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
	
	public static Movie constructMovieFromResultSet(ResultSet rs){
		try {
			Movie movie = new Movie();
			movie.setId(rs.getInt(ID_COLUMN));
			movie.setName(rs.getString(NAME_COLUMN));
			movie.setYear(rs.getString(PUBLISHED_YEAR_COLUMN));
			String type = rs.getString(TYPE_COLUMN);
			if(type != null){
				movie.setType(Arrays.asList(type.split(", ")));
			}
			return movie;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<Movie> getMovies(Collection<String> movieIDs){
		List<Movie> movies = new ArrayList<Movie>();
		String movieIDString = StringUtil.connectString(movieIDs, ", ");
		
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID_COLUMN + " IN ( " + movieIDString + " )";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnectionFromDataSource();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
//			conn = DBUtil.getConnection();
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();
			while (rs.next()) {
				Movie movie = constructMovieFromResultSet(rs);
				if(movie != null){
					movies.add(movie);
				}
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
	
	public static Map<String, Movie> getMovieMap(Collection<String> movieIDs){
		Map<String, Movie> movies = new HashMap<String, Movie>();
		String movieIDString = StringUtil.connectString(movieIDs, ", ");
		
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID_COLUMN + " IN ( " + movieIDString + " )";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnectionFromDataSource();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
//			conn = DBUtil.getConnection();
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();
			while (rs.next()) {
				Movie movie = constructMovieFromResultSet(rs);
				if(movie != null){
					movies.put(String.valueOf(movie.getId()), movie);
				}
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
	
	public static List<Movie> getAllMovies(){
		List<Movie> movies = new ArrayList<Movie>();
		
		String sql = "SELECT * FROM " + TABLE_NAME;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
//			conn = DBUtil.getConnectionFromDataSource();
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Movie movie = constructMovieFromResultSet(rs);
				if(movie != null){
					movies.add(movie);
				}
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
}
