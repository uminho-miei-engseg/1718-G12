/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auctions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author mariana
 */
public class DB_Connection {

	public final static String DB_DRIVER_CLASS = "com.mysql.jdbc.Driver";
	public final static String DB_URL = "jdbc:mysql://localhost/auctions?autoReconnect=true&useSSL=false";
	public final static String DB_USERNAME = "auctions_admin";
	public final static String DB_PASSWORD = "passw713";

	public static Connection getConnection() throws ClassNotFoundException, SQLException {

		Connection conn = null;
		Class.forName(DB_DRIVER_CLASS);
		conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
		System.out.println("DB Connection created successfully");
		return conn;
	}
}

