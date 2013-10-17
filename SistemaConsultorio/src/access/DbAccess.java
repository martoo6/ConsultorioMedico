package access;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;


public class DbAccess{
	// Singleton Stuff
	private static class SingletonHolder { 
		public static final DbAccess INSTANCE = new DbAccess();
	}
	public static DbAccess getInstance() {
		return SingletonHolder.INSTANCE;
	}
	// End Singleton Stuff

	private Connection conn;
	private Statement stmt;

	public void initilaize(String file){
		try{
			String sDriverName = "org.sqlite.JDBC";
			Class.forName(sDriverName);

			//String sTempDb = "db.sqlite";
			String sJdbc = "jdbc:sqlite";
			//String sDbUrl = sJdbc + ":" + sTempDb;
			String sDbUrl = sJdbc + ":" + file;

			conn = DriverManager.getConnection(sDbUrl);
			stmt = conn.createStatement();
		}catch(Exception e){
			e.printStackTrace();
		}		

	}

	public DefaultTableModel execute(String query){
		try {
			//stmt.setQueryTimeout(30);
			return buildTableModel(stmt.executeQuery(query));
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public void executeNoReturn(String query){
		try {
			stmt.executeUpdate(query);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public void close(){
		try{
			stmt.close();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {

	    ResultSetMetaData metaData = rs.getMetaData();

	    // names of columns
	    Vector<String> columnNames = new Vector<String>();
	    int columnCount = metaData.getColumnCount();
	    for (int column = 1; column <= columnCount; column++) {
	        columnNames.add(metaData.getColumnName(column));
	    }

	    // data of the table
	    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	    while (rs.next()) {
	        Vector<Object> vector = new Vector<Object>();
	        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
	            vector.add(rs.getObject(columnIndex));
	        }
	        data.add(vector);
	    }

	    return new DefaultTableModel(data, columnNames);

	}
	
	public static int convToInt(String string){
		string=string.replaceAll("[^0-9]", "");
		if(string.length()==0){
			return 0;
		}
		return Integer.parseInt(string);
	}
	
	public static long convToLong(String string){
		string=string.replaceAll("[^0-9]", "");
		if(string.length()==0){
			return 0;
		}
		return Long.parseLong(string);
	}
	
	public static String toString(Object obj){
		if(obj==null){
			return "";
		}
		return obj.toString();
	}
	public static Long toLong(Object obj){
		if(obj==null){
			return (long) 0;
		}
		return Long.parseLong(obj.toString());
	}
	public static int toInt(Object obj){
		if(obj==null){
			return (int) 0;
		}
		return Integer.parseInt(obj.toString());
	}
}
