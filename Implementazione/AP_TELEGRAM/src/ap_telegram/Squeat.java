/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ap_telegram;

import ap_communication.ClientConnector;
import ap_utility.ConfigurationLoader;
import ap_utility.Utility;
import ap_utility.WebUtility;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 *
 * @author ribon
 */
public class Squeat extends TelegramLongPollingBot {

    public Squeat() {

    }

    @Override
    public String getBotToken() {
        return ConfigurationLoader.getNodeValue("botkey");
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update);
        //Ho ricevuto una posizione
        if (update.getMessage() != null && update.getMessage().getLocation() != null) {
            long mittente = update.getMessage().getChatId();
            float lat = update.getMessage().getLocation().getLatitude();
            float lon = update.getMessage().getLocation().getLongitude();
            SendMessage[] restaurants = BotLogic.sendRestaurant(mittente, lat, lon);
            for (SendMessage restaurant : restaurants) {
                send(restaurant);
            }
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            long mittente = update.getMessage().getChatId();
            Message received = update.getMessage();
            System.out.println(received);
            BotLogic.callAction(received.getText(), mittente);
            String[] answers = BotLogic.getAnswer(received.getText(), mittente, BotLogic.getStatus(mittente));
            processAnswers(received.getChat().getFirstName(), mittente, answers);
        } else if (update.hasCallbackQuery()) {
            long mittente = update.getCallbackQuery().getMessage().getChatId();
            String received = update.getCallbackQuery().getData();
            System.out.println("received: " + received);
            BotLogic.callAction(received, mittente);
            String[] answers = BotLogic.getAnswer(received, mittente, BotLogic.getStatus(mittente));
            processAnswers(update.getCallbackQuery().getMessage().getChat().getFirstName(), mittente, answers);
        }
    }

    @Override
    public String getBotUsername() {
        return "SquEatBot";
    }

    private void processAnswers(String name, long ChatID, String[] answers) {
        SendMessage msg = new SendMessage().setChatId(ChatID);
        SendPhoto pic = new SendPhoto().setChatId(ChatID);
        for (String message : answers) {
            System.out.println("messaggio: " + message);
            if (message.contains("%CHATNAME")) {
                message = message.replace("%CHATNAME", name);
            }
            if (message.contains("%IMGWELCOME")) {
                send(pic.setPhoto("AgADBAADbbAxG1cHWFMev4i89OXkaHbYLBsABL1RKopBZwJjmJsAAgI"));
                continue;
            }
            if (message.contains("%LOGINKB")) {
                message = message.replace("%LOGINKB", "");
                BotLogic.setKeyboard(msg, "LogReg");
            }
            if (message.contains("%HOMEPAGEKB")) {
                message = message.replace("%HOMEPAGEKB", "");
                BotLogic.setKeyboard(msg, "homepage");
            }
            if (message.startsWith("IMG")) {
                message = message.replace("IMG", "");
                String pos = message.substring(0, message.indexOf("*"));
                message = message.replace(pos, "");
                File photo = WebUtility.getImage(pos);
                if (photo != null) {
                    send(new SendPhoto().setChatId(ChatID).setPhoto(photo));
                }
            }
            String[] data = message.split(";");
            if(data.length == 4 && Utility.areNumbers(data[1], data[2], data[3])){ //aggiungo la tastiera per il prodotto
                String idlocale = data[1], idprodotto = data[2], costo = data[3];
                message = message.replace(";"+idlocale+";"+idprodotto+";"+costo, "");
                BotLogic.setFoodKeyboard(msg, idlocale, idprodotto, costo);
            }
            send(msg.setText(message).enableMarkdown(true));
        }
    }

    private void send(SendMessage msg) {
        try {
            execute(msg);
        } catch (TelegramApiException ex) {
            Logger.getLogger(Squeat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void send(SendPhoto img) {
        try {
            execute(img);
        } catch (TelegramApiException ex) {
            Logger.getLogger(Squeat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
