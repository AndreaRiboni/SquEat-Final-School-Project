/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telegrambottest;

import com.mysql.jdbc.Connection;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 *
 * @author Utente
 */
public class Reebot extends TelegramLongPollingBot {

    private Statement query;
    private ResultSet result;
    private Connection conn;

    public Reebot() throws SQLException {
        conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/telegram?user=root&password=");
        query = conn.createStatement();
    }

    @Override
    public String getBotToken() {
        return "";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            try {
                checkUser(update.getMessage().getChatId(), update.getMessage().getText());
            } catch (Exception ex) {
                Logger.getLogger(Reebot.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.out.print(update.getMessage().getChatId() + " dice: ");
            System.out.println(update.getMessage().getText());
        }
    }

    @Override
    public String getBotUsername() {
        return "AndreaRBot";
    }

    /**
     *
     * @param id
     * @return <ul><li>0: DEVE INSERIRE NOME</li><li>1: NON HA
     * RICHIESTE</li></ul>
     * @throws Exception
     */
    private synchronized int checkUser(long id, String message) throws Exception {
        SendMessage msg;
        result = query.executeQuery("select nome from utente where id = '" + id + "'");
        //se l'utente non esiste, lo registro
        if (!result.next()) {
            msg = new SendMessage(id, "Come ti chiami?");
            execute(msg);
            query.execute("insert into utente values (" + id + ", '', 0)");
            return 0;
        } else { //È registrato. Vedo lo stato dell'utente
            result = query.executeQuery("select stato from utente where id = '" + id + "'");
            result.next();
            int stato = result.getInt("stato");
            //Se vale 0, ha inserito il nome
            if (stato == 0) {
                query.execute("update utente set nome = '" + message + "', stato = 1 where id = " + id);
                msg = new SendMessage(id, "Okay. D'ora in poi sarai " + message + ". Puoi ora inviare URL.");
                execute(msg);
            } else { // Altrimenti, invio la richiesta
                File file = getPage(id, message);
                if (file == null) {
                    msg = new SendMessage(id, "L'URL inserito non è valido.");
                    execute(msg);
                } else {
                    SendDocument doc = new SendDocument();
                    doc.setChatId(id);
                    doc.setDocument(file);
                    doc.setCaption("Ecco la pagina richiesta.");
                    execute(doc);
                }

            }
            return 1;
        }
    }
    
    private synchronized File getPage(long id, String msg) {
        try {
            String html = WebUtility.getHTMLPage(WebUtility.getPlainURL(msg));
            File file = new File(id+".html");
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(html);
            bw.close();
            return file;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
