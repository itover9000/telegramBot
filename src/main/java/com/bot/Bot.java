package com.bot;

import com.exception.InvalidDataFormatException;
import com.exception.InvalidUrlException;
import com.exception.NoDataOnSiteException;
import com.service.BoredApi;
import com.service.Currency;
import com.service.GeomagneticStorm;
import com.settings.BotSetting;
import com.settings.UrlSetting;
import com.util.MeteoradarUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class Bot extends TelegramLongPollingBot {
    private static final Logger logger = LogManager.getLogger(Bot.class);
    private static final String ERROR_MESSAGE = "An exception occurred!";
    private static final String MESSAGE_ANSWER = "Не удалось получить данные";

    private final MeteoradarUtil meteoradarUtil;

    private final BotSetting botSetting;
    private final GeomagneticStorm geomagneticStorm;
    private final BoredApi boredApi;
    private final Currency currency;

    private final UrlSetting urlSetting;


    @Autowired
    public Bot(GeomagneticStorm geomagneticStorm, BoredApi boredApi, Currency currency,
               BotSetting botSetting, MeteoradarUtil meteoradarUtil, UrlSetting urlSetting) {
        this.geomagneticStorm = geomagneticStorm;
        this.boredApi = boredApi;
        this.currency = currency;
        this.botSetting = botSetting;
        this.meteoradarUtil = meteoradarUtil;
        this.urlSetting = urlSetting;
    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        if (message != null && message.hasText()) {
            switch (message.getText().toLowerCase().strip()) {
                case "магн. буря" ->{
                    try {
                        sendMsg(message, geomagneticStorm.getGeomagneticStorm());
                    } catch (IOException | NoDataOnSiteException e) {
                        sendMsg(message, MESSAGE_ANSWER);
                        logger.error(ERROR_MESSAGE, e);
                    } catch (InvalidDataFormatException e) {
                        sendMsg(message, "Неверный формат данных");
                        logger.error(ERROR_MESSAGE, e);
                    }
                }
                case "текущая погода" ->{
                    try {
                        //send a picture from the site, mark the place on the map
                        execute(new SendPhoto()
                                .setPhoto(new File(meteoradarUtil.getMapWithVillage(urlSetting.getGifFileNameFromMeteoinfo())))
                                .setChatId(message.getChatId().toString()));

                        //picture time message
                        sendMsg(message, "погода " + meteoradarUtil.getTimeFromSiteWithNewTime(meteoradarUtil.getTitle()));
                    } catch (TelegramApiException | IOException | ParseException | InvalidUrlException | NoDataOnSiteException e) {
                        sendMsg(message, MESSAGE_ANSWER);
                        logger.error(ERROR_MESSAGE, e);
                    }
                }
                case "анимация погоды" ->{
                    try {
                        //send animation about the weather for the last 3 hours
                        execute(new SendVideo()
                                .setVideo(new File(
                                        meteoradarUtil.getPathToFileInRootProject(
                                                urlSetting.getUrlToGifFile(),
                                                urlSetting.getGifFileNameFromMeteoinfo())))
                                .setChatId(message.getChatId().toString()));
                    } catch (TelegramApiException | IOException | InvalidUrlException e) {
                        sendMsg(message, MESSAGE_ANSWER);
                        logger.error(ERROR_MESSAGE, e);
                    }
                }
                case "скучно!" ->{
                    try {
                        sendMsg(message, boredApi.getBoredStringFormat());
                    } catch (IOException e) {
                        sendMsg(message, MESSAGE_ANSWER);
                        logger.error(ERROR_MESSAGE, e);
                    }
                }
                case "курс валют" ->{
                    try {
                        sendMsg(message, currency.getCurrency());
                    } catch (IOException e) {
                        sendMsg(message, MESSAGE_ANSWER);
                        logger.error(ERROR_MESSAGE, e);
                    }
                }
                default ->sendMsg(message, "Неизвестная команда");
            }
        }
    }

    private void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);

        setButton(sendMessage);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.error(ERROR_MESSAGE, e);
        }
    }

    private void setButton(SendMessage message) {
        // button size settings
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        message.setReplyMarkup(keyboardMarkup);
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        //add in keyboardRows rows buttons from yaml
        botSetting.getButtons().forEach((key, values) -> {
            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.addAll(values);
            keyboardRows.add(keyboardRow);
        });

        keyboardMarkup.setKeyboard(keyboardRows);
    }

    public String getBotUsername() {
        return botSetting.getUsername();
    }

    public String getBotToken() {
        return botSetting.getToken();
    }
}
