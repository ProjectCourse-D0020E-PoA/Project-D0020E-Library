package Database;

import Library.KeyEncodeDecode;

import java.security.Key;
import java.sql.*;

public class Getters {

    /**
     * @param Name The primary key in the database
     * @return Key
     * @author Isak Forsgren
     */
    public static Key getPriv(String Name){

        String resString = null;

        try {
            Connection c = DB.Connect();

            String sql = "SELECT * FROM " + DB.table + " WHERE Name = ?";

            PreparedStatement preparedStmt = c.prepareStatement(sql);
            preparedStmt.setString(1,Name);

            ResultSet res = preparedStmt.executeQuery();

            //goes trew the entire result set and stores them to a varible
            //in this case its only 1
            while(res.next())
                resString = res.getString(2);

            res.close();
            preparedStmt.close();
            c.close();

        }catch (SQLException e){
            System.out.println(e.getErrorCode());
        }
        return KeyEncodeDecode.decodeKeyBytesPrivate(resString);
    }

    /**
     * @param Name The primary key in the database
     * @return Key
     * @author Isak Forsgren
     */
    public static Key getPub(String Name){

        String resString = null;

        try {
            Connection c = DB.Connect();

            String sql = "SELECT * FROM " + DB.table + " WHERE Name = ?";

            PreparedStatement preparedStmt = c.prepareStatement(sql);

            preparedStmt.setString(1,Name);

            ResultSet res = preparedStmt.executeQuery();

            //goes trew the entire result set and stores them to a varible
            //in this case its only 1
            while(res.next())
                resString = res.getString(3);

            res.close();
            preparedStmt.close();
            c.close();

        }catch (SQLException e){
            System.out.println(e.getErrorCode());
        }
        return KeyEncodeDecode.decodeKeyBytesPublic(resString);
    }

    /**
     * untested fuction did not have accses to db
     * @param Name The primary key in the database
     * @return ip
     * @author Isak Forsgren
     */

    public static String getIp(String Name){

        String resString = null;

        try {
            Connection c = DB.Connect();

            String sql = "SELECT * FROM " + DB.table + " WHERE Name = ?";

            PreparedStatement preparedStmt = c.prepareStatement(sql);
            preparedStmt.setString(1,Name);

            ResultSet res = preparedStmt.executeQuery();

            //goes trew the entire result set and stores them to a varible
            //in this case its only 1
            while(res.next())
                resString = res.getString(4);

            res.close();
            preparedStmt.close();
            c.close();

        }catch (SQLException e){
            System.out.println(e.getErrorCode());
        }
        return resString;
    }

    /**
     * untested fuction did not have accses to db
     * @param Name The primary key in the database
     * @return port
     * @author Isak Forsgren
     */

    public static String getPort(String Name){

        String resString = null;

        try {
            Connection c = DB.Connect();

            String sql = "SELECT * FROM " + DB.table + " WHERE Name = ?";

            PreparedStatement preparedStmt = c.prepareStatement(sql);
            preparedStmt.setString(1,Name);

            ResultSet res = preparedStmt.executeQuery();

            //goes trew the entire result set and stores them to a varible
            //in this case its only 1
            while(res.next())
                resString = res.getString(5);

            res.close();
            preparedStmt.close();
            c.close();

        }catch (SQLException e){
            System.out.println(e.getErrorCode());
        }
        return resString;
    }

}
