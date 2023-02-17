package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DB {

    public static String table = "key_db.keys";

    /**
     * Attempts to connect to a localhost mysql db if that fails
     * <p>
     * Attempt to connect to the a docker db (this is in the case the application is running in a docker container)
     * <p>
     * @return Connection
     * @author Isak Forsgren
     */
    public static Connection Connect(){
        Connection conn = null;

        //try to connect to db at localhost:3306 and if it failed presume it's
        // running in docker and try to connect in the container.
        try {


            conn = DriverManager.getConnection("jdbc:mysql://0.0.0.0:3307/key_db?" +
                            "user=bob&password=bob123!");

            // Do something with the Connection

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            try {
                conn = DriverManager.getConnection("jdbc:mysql://db:3306/key_db?" +
                        "user=bob&password=bob123!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    return conn;
    }

    /**Creates a table in the database with the columns
     *<blockquote><pre>
     *Name      String  Index 1
     *Priv_key  String  Index 2
     *Pub_key   String  Index 3
     *ip        String  Index 4
     *port      Int     Index 5
     *</pre></blockquote><p>
     * Where Name is the primary key, and all the columns have the NOT NULL attribute
     * @author Isak Forsgren
     */

    public static void createTable() {

        Connection c = DB.Connect();

        String sql = "CREATE TABLE IF NOT EXISTS "
                   + table
                   + "  (Name VARCHAR(20) NOT NULL,"
                   + "  Priv_key TEXT NOT NULL,"
                   + "  Pub_key TEXT NOT NULL,"
                   + "  ip VARCHAR(32) NOT NULL,"
                   + "  port INT(11) UNSIGNED NOT NULL,"
                   + "  PRIMARY KEY(`Name`(20)))";

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
