package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DB {

    public static String table = "key_db.keys";

    public static Connection Connect(){
    Connection conn = null;
        try {
        conn =
                DriverManager.getConnection("jdbc:mysql://db:3306/key_db?" +
                        "user=bob&password=bob123!");

        // Do something with the Connection

    } catch (SQLException ex) {
        // handle any errors
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
    }

    return conn;
    }

    public static void createTable() {

        Connection c = DB.Connect();
        String sql = "CREATE TABLE IF NOT EXISTS " + table + "(\n" +
                "  Name VARCHAR(20) NOT NULL,\n" +
                "  Priv_key VARCHAR(1024) NOT NULL,\n" +
                "  Pub_key VARCHAR(1024) NOT NULL,\n" +
                "  ip BINARY(32) NOT NULL,\n" +
                "  port INT(11) UNSIGNED NOT NULL\n" +
                ")\n";
        System.out.println(sql);
        try {
            Statement statement = c.createStatement();
            statement.execute(sql);
            statement.close();
            c.close();
            System.out.println("Created table in given database...");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
