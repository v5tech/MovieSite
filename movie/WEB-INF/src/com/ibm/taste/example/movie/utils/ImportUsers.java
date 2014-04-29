package com.ibm.taste.example.movie.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ibm.taste.example.movie.model.User;
import com.ibm.taste.example.movie.utils.DBUtil;

public class ImportUsers {

	public final static String TABLE_NAME = "users";
	public final static String ID_COLUMN = "id";
	public final static String NAME_COLUMN = "name";
	public final static String EMAIL_COLUMN = "email";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Connection conn = DBUtil.getJDBCConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<User> userList = new ArrayList<User>();
		String sql = "select distinct(userid) from movie_preferences";
		int flag = 0;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			User user = null;
			while(rs.next())
			{
				user = new User();
				flag = rs.getInt("userId");
				user.setId(flag);
				user.setName("test" + flag);
				user.setEmail("test" + flag + "@gmail.com");
				userList.add(user);
			}		
			insertUsers(userList);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.clearWarnings();
				rs.close();
				ps.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
	}

	public static void insertUsers(List<User> users){
		Connection conn = DBUtil.getJDBCConnection();
		PreparedStatement ps = null;
		String sql = "insert into "
				+ TABLE_NAME + " ( "
				+ ID_COLUMN + ", "
				+ NAME_COLUMN + ", "
				+ EMAIL_COLUMN 
				+ ") values (?, ?, ?)";
		try {
			conn.setAutoCommit(false);

			ps = conn.prepareStatement(sql);

			for (User user : users) {
				ps.setInt(1, user.getId());
				ps.setString(2, user.getName());
				ps.setString(3, user.getEmail());
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
}
