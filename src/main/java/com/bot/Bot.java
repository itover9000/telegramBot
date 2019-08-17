package com.bot;

import com.exception.InvalidUrlException;
import com.exception.NoDataOnTheSiteException;
import com.model.BoredModel;
import com.service.BoredApi;
import com.service.Currency;
import com.service.GeomagneticStorm;
import com.settings.BotSetting;
import com.settings.UrlSetting;
import com.util.MeteoradarUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class Bot extends TelegramLongPollingBot {

    private final MeteoradarUtil meteoradarUtil;

    private final BotSetting botSetting;
    private final BoredModel boredModel;
    private final GeomagneticStorm geomagneticStorm;
    private final BoredApi boredApi;
    private final Currency currency;

    private final UrlSetting urlSetting;

    @Autowired
    public Bot(BoredModel boredModel, GeomagneticStorm geomagneticStorm, BoredApi boredApi,
               Currency currency, BotSetting botSetting, MeteoradarUtil meteoradarUtil, UrlSetting urlSetting) {
        this.boredModel = boredModel;
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
            switch (message.getText().toLowerCase().trim()) {
                case "магн. буря":
                    try {
                        sendMsg(message, geomagneticStorm.getGeomagneticStorm());

                    } catch (IOException e) {
                        sendMsg(message, "что-то пошло не так");
                        e.printStackTrace();
                    }
                    break;

                case "текущая погода":
                    try {
                        //пересылаю картинку с сайта
                        //предварительно помечаю место на карте
                        execute(new SendPhoto()
                                .setPhoto(new File(meteoradarUtil.getMapWithVillage("radar-map.png")))
                                .setChatId(message.getChatId().toString()));

                        //сообщение о времени картинки
                        sendMsg(message, "погода " + meteoradarUtil.getTimeFromSiteWithNewTime(meteoradarUtil.getTitle()));
                    } catch (TelegramApiException | IOException | ParseException e) {
                        sendMsg(message, "данные на сайте недоступны");
                        e.printStackTrace();
                    } catch (InvalidUrlException | NoDataOnTheSiteException e) {
                        sendMsg(message, "Сайт не предоставил данные");
                        e.printStackTrace();
                    }
                    break;

                case "анимация погоды":
                    try {
                        //send animation about the weather for the last 3 hours
                        execute(new SendVideo()
                                .setVideo(new File(
                                        meteoradarUtil.getPathToFileInRootProject(
                                                urlSetting.getUrlToGifFile(), urlSetting.getGifFileNameFromMeteoinfo())))
                                .setChatId(message.getChatId().toString()));
                    } catch (TelegramApiException e) {
                        sendMsg(message, "что-то пошло не так");
                    } catch (IOException | InvalidUrlException e) {
                        sendMsg(message, "данные на сайте недоступны");
                        e.printStackTrace();
                    }
                    break;
                case "скучно!":
                    try {
                        sendMsg(message, boredApi.getBoredStringFormat(boredModel));
                    } catch (IOException e) {
                        sendMsg(message, "что-то пошло не так");
                        e.printStackTrace();
                    }
                    break;
                case "курс валют":
                    try {
                        sendMsg(message, currency.getCurrency());
                    } catch (IOException e) {
                        sendMsg(message, "что-то пошло не так");
                        e.printStackTrace();
                    }
                    break;

                default:
                        sendMsg(message, ".");

            }
        }
    }

    private void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);

        try {
            setButton(sendMessage);
            execute(sendMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void setButton(SendMessage message) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        message.setReplyMarkup(keyboardMarkup);
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("магн. буря"));
        keyboardFirstRow.add(new KeyboardButton("скучно!"));
        keyboardFirstRow.add(new KeyboardButton("курс валют"));
        keyboardSecondRow.add(new KeyboardButton("текущая погода"));
        keyboardSecondRow.add(new KeyboardButton("анимация погоды"));

        keyboardRows.add(keyboardFirstRow);
        keyboardRows.add(keyboardSecondRow);
        keyboardMarkup.setKeyboard(keyboardRows);


    }

    public String getBotUsername() {
        return botSetting.getUsername();
    }

    public String getBotToken() {
        return botSetting.getToken();
    }
}
