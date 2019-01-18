/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telegrambottest;

import java.sql.SQLException;
import java.util.Scanner;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 *
 * @author Utente
 */
public class TelegramBotTest {

    public static void main(String[] args) throws SQLException {
        //Scarico i driver di MySQL
        try {
            Class.forName("org.gjt.mm.mysql.Driver");
        } catch (ClassNotFoundException ex) {
            System.err.println("Non ho trovato i driver di sql. Il programma verrà chiuso.");
            return;
        }
        
        //Inizializzo le API di Telegram
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        
        //Creo il BOT
        Reebot bot = new Reebot();

        try {
            botsApi.registerBot(bot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
