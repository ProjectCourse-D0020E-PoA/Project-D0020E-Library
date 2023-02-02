package Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Getters {

    public void getPriv(String Name) throws SQLException {
        Connection c = DB.Connect();
        String sql = "SELECT Priv_key FROM " + DB.table + " WHERE Name = '" + Name + "'";
        Statement statement = c.createStatement();
        statement.execute(sql);
        statement.close();
        c.close();
    }

}
