package ap_server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andrea Riboni
 */
public class Database {

    private static boolean isSetted = false;
    private static Connection conn;
    private static Statement query;
    private static PreparedStatement PrepQuery;
    private static ResultSet result;

    private static void init() {
        if (!isSetted) {
            try {
                conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/areaprogetto?user=root&password=Az-72165");
                query = conn.createStatement();
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
            isSetted = true;
        }
    }

    public static ArrayList<String[]> stringify(ResultSet res) {
        try {
            int columns = result.getMetaData().getColumnCount();
            ArrayList<String[]> table = new ArrayList<>();
            while (result.next()) {
                String[] row = new String[columns];
                for (int i = 0; i < columns; i++) {
                    row[i] = result.getString(i + 1);
                }
                table.add(row);
            }
            return table;
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static ResultSet select(String query) {
        init();
        try {
            return Database.query.executeQuery(query);
        } catch (SQLException ex) {
            return null;
        }
    }

    public static int count(ResultSet res) {
        try {
            res.last();
            return res.getRow();
        } catch (SQLException ex) {
            ex.printStackTrace();
           return 0;
        }
    }
}
