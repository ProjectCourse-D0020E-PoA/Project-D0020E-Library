package Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Update {
    public void SetOwnPriv() throws SQLException {
        Connection c = DB.Connect();
        String sql = "Update " + DB.table + " where id = 1";
        Statement statement = c.createStatement();
        statement.execute(sql);
        statement.close();
        c.close();
    }
}
