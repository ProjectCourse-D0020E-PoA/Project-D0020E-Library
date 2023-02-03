package Database;

import Library.KeyEncDec;

import java.security.Key;
import java.sql.*;

public class Getters {

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

            //temp
            System.out.println(resString);

        }catch (SQLException e){
            System.out.println(e.getErrorCode());
        }
        return KeyEncDec.decodeKeyBytesPrivate(resString);
    }

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
        return KeyEncDec.decodeKeyBytesPublic(resString);
    }


}
