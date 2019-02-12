package ap_database;

import ap_utility.ConfigurationLoader;
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

    private Connection conn;
    private Statement query;
    private PreparedStatement PrepQuery;
    private ResultSet result;

    private static String NOME_DB, USER, IP, PSW;

    public Database() {
            NOME_DB = ConfigurationLoader.getNodeValue("dbname");
            USER = ConfigurationLoader.getNodeValue("dbuser");
            IP = ConfigurationLoader.getNodeValue("dbip");
            PSW = ConfigurationLoader.getNodeValue("dbpsw");
        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + IP + ":3306/" + NOME_DB + "?user=" + USER + "&password=" + PSW);
            query = conn.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<String[]> stringify(ResultSet res) {
        try {
            int columns = res.getMetaData().getColumnCount();
            ArrayList<String[]> table = new ArrayList<>();
            while (res.next()) {
                String[] row = new String[columns];
                for (int i = 0; i < columns; i++) {
                    row[i] = res.getString(i + 1);
                }
                table.add(row);
            }
            return table;
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ResultSet select(String query) {
        try {
            return this.query.executeQuery(query);
        } catch (SQLException ex) {
            return null;
        }
    }

    public int count(ResultSet res) {
        try {
            res.last();
            return res.getRow();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public boolean register(String[] msg) {
        try {
            query.executeUpdate(String.format("insert into utente (Nome, Cognome, Cellulare, Indirizzo, Mail, Psw, Privilegio)"
                    + "values ('%1$s', '%2$s', '%3$s', '%4$s', '%5$s', '%6$s', '%7$s')", msg[3], msg[4], msg[5], msg[6], msg[1], msg[2], msg[7]));
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    public boolean addLocale(String[] msg) {
        try {
            query.executeUpdate(String.format("insert into locale (Nome, Indirizzo, Cellulare, IDAdmin, Punteggio, NumRecensioni)"
                    + "values ('%1$s', '%2$s', '%3$s', '%4$s', '%5$s', '%6$s')", msg[1], msg[2] + ", " + msg[3], msg[4], msg[5], "0", "0"));
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    
    public int update(String query){
        try {
            return this.query.executeUpdate(query);
        } catch (SQLException ex) {
            return -1;
        }
    }
    
    
}
