package Database;

import Library.KeyEncodeDecode;

import java.security.Key;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class to handle the setters needed for the application to interact with out database
 * @author Isak Forsgren
 */
public class Setters {
    /**
     * InsertNew Takes everything stored in the database as parameters and adds a new entry in to the table
     *
     * @param Name acting like the primary key in the data base
     * @param Priv_key (RS256) encoded to String format
     * @param Pub_key (RS256) encoded to String format
     * @param ip address in a string for to allow for localhost
     * @param port number representing the port that's supposed to be used for communications
     *
     * @author Isak Forsgren
     */
    public static void InsertNew(
            String Name,
            String Priv_key,
            String Pub_key,
            String ip,
            int port) {


        String sql = "INSERT INTO " + DB.table
                   + " (Name, Priv_key, Pub_key, ip, port)"
                   + " values (?, ?, ?, ?, ?)";

        try {
            Connection c = DB.Connect();
            PreparedStatement preparedStmt = c.prepareStatement(sql);

            preparedStmt.setString(1, Name);
            preparedStmt.setString(2, Priv_key);
            preparedStmt.setString(3, Pub_key);
            preparedStmt.setString(4,ip);
            preparedStmt.setInt   (5,port);

            preparedStmt.execute();
            preparedStmt.close();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    /**
     * UpdateKeys Updates the entry where the Name == @param Name
     *
     * @param Name acting like the primary key in the data base
     * @param Priv_key (RS256) encoded to String format
     * @param Pub_key (RS256) encoded to String format
     *
     * @author Isak Forsgren
     */
    public static void UpdateKeys(
            String Name,
            String Priv_key,
            String Pub_key){

        String sql = "UPDATE " + DB.table
                   + " SET Priv_key = ?, Pub_key = ?"
                   + " WHERE Name = ?";

        try {
            Connection c = DB.Connect();
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setString(1,Priv_key);
            stmt.setString(2,Pub_key);
            stmt.setString(3,Name);

            stmt.executeUpdate();
            stmt.close();
            c.close();

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * UpdateIP Updates the entry where the Name == @param Name
     *
     * @param Name like the primary key in the data base
     * @param ip address in a string for to allow for localhost
     * @param port number representing the port that's supposed to be used for communications
     *
     * @author Isak Forsgren
     */
    public static void UpdateIP(
            String Name,
            String ip,
            String port){

        String sql = "UPDATE " + DB.table
                + " SET ip = ?, port = ?"
                + " WHERE Name = ?";

        try {
            Connection c = DB.Connect();
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setString(1,ip);
            stmt.setString(2,port);
            stmt.setString(3,Name);

            stmt.executeUpdate();
            stmt.close();
            c.close();

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * This Seams like a test method, might need to be Deleted
     * @return Key
     * @author Isak Forsgren
     * @throws SQLException
     */
    public void SetOwnPriv() throws SQLException {
        Connection c = DB.Connect();
        String sql = "Update " + DB.table + " where id = 1";
        Statement statement = c.createStatement();
        statement.execute(sql);
        statement.close();
        c.close();
    }
}
