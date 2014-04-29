package com.ibm.taste.example.movie.recommender;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;

import com.ibm.taste.example.movie.utils.DBUtil;

public class MovieDataModel extends MySQLJDBCDataModel{
	public final static String PERFERENCETABLE = "movie_preferences";
	public final static String USERID_COLUMN = "userID";
	public final static String ITEMID_COLUMN = "movieID";
	public final static String PERFERENCE_COLUMN = "preference";
	

	public MovieDataModel(String dataSourceName) throws TasteException {		
		super(lookupDataSource(dataSourceName), PERFERENCETABLE, USERID_COLUMN, ITEMID_COLUMN, PERFERENCE_COLUMN);
	}

	public MovieDataModel() {
		super(DBUtil.getDataSource(), PERFERENCETABLE, USERID_COLUMN, ITEMID_COLUMN, PERFERENCE_COLUMN);
	}
}
