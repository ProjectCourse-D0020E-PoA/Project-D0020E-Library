package Database;

import Database.DB.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Get {

    public void getOwnPriv() throws SQLException {
        Connection c = DB.Connect();
        String sql = "SELECT Priv_key FROM " + DB.table + " WHERE Name IN (self)";
        Statement statement = c.createStatement();
        statement.execute(sql);
        statement.close();
        c.close();
    }

}
