/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ap_telegram;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;

/**
 *
 * @author ribon
 */
public class AP_TELEGRAM {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();

        //Creo il BOT
        Squeat bot = new Squeat();

        try {
            botsApi.registerBot(bot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
