package TempleateRDB.GenericTempleate;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author wilmer-PC
 */
public abstract class Generic {
    
    public abstract void loadDatabase(Connection con) throws SQLException;
    
    public abstract String DatabaseToSql();
    
    public abstract String DatabaseToSqlWithData(Connection con) throws SQLException;
}
