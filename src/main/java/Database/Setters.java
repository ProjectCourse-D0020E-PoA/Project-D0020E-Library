package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Setters {

    public void SetOwnPriv() throws SQLException {
        Connection c = DB.Connect();
        String sql = "Update " + DB.table + " where id = 1";
        Statement statement = c.createStatement();
        statement.execute(sql);
        statement.close();
        c.close();
    }


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
            preparedStmt.setString(2,Priv_key);
            preparedStmt.setString(3,Pub_key);
            preparedStmt.setString(4,ip);
            preparedStmt.setInt   (5,port);

            preparedStmt.execute();
            preparedStmt.close();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    public static void UpdateKeys(
            String Name,
            String Priv_key,
            String Pub_key){

        String sql = "UPDATE " + DB.table
                   + " set Priv_key = ? AND Pub_key = ?"
                   + " WHERE Name = ?";

        try {
            Connection c = DB.Connect();
            PreparedStatement stmt = c.prepareStatement(sql);

            stmt.setString(1,Name);
            stmt.setString(2,Priv_key);
            stmt.setString(3,Pub_key);

            stmt.execute();
            stmt.close();
            c.close();

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
