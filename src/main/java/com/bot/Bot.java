package com.bot;

import com.model.BoredModel;
import com.model.CurrencyModel;
import com.model.GeomagneticStormModel;
import com.model.WeatherModel;
import com.settings.ApplicationSetting;
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
import com.service.BoredApi;
import com.service.Currency;
import com.service.GeomagneticStorm;
import com.service.Weather;
import com.util.MeteoradarUtil;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Component
//@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class Bot extends TelegramLongPollingBot {

    private final ApplicationSetting setting;
    private final WeatherModel weatherModel;
    private final CurrencyModel currencyModel;
    private final BoredModel boredModel;
    private final GeomagneticStormModel stormModel;
    private final GeomagneticStorm geomagneticStorm;
    private final BoredApi boredApi;
    private final Currency currency;

    @Autowired
    public Bot(WeatherModel weatherModel, CurrencyModel currencyModel, BoredModel boredModel, GeomagneticStormModel stormModel, GeomagneticStorm geomagneticStorm, BoredApi boredApi, Currency currency, ApplicationSetting setting) {
        this.weatherModel = weatherModel;
        this.currencyModel = currencyModel;
        this.boredModel = boredModel;
        this.stormModel = stormModel;
        this.geomagneticStorm = geomagneticStorm;
        this.boredApi = boredApi;
        this.currency = currency;
        this.setting = setting;
    }


    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        if (message != null && message.hasText()) {
            switch (message.getText().toLowerCase().trim()) {
                case "магн. буря":
                    try {
                        sendMsg(message, geomagneticStorm.getGeomagneticStorm(stormModel));

                    } catch (IOException e) {
                        sendMsg(message, "что-то пошло не так");
                        System.out.println(e.toString());
                    }
                    break;

                case "текущая погода":
                    try {
                        //пересылаю картинку с сайта
                        execute(new SendPhoto()
                                .setPhoto(MeteoradarUtil.getImageFromUrl())
                                .setChatId(message.getChatId().toString()));

                        //сообщение о времени картинки
                        sendMsg(message, "состояние на " + MeteoradarUtil.getTimeFromSite());
                    } catch (TelegramApiException | IOException | ParseException e) {
                        sendMsg(message, "что-то пошло не так");
                    }
                    break;

                case "анимация погоды":
                    try {
                        //пересылаю анимацию о погоде за последние 3 часа
                        //Качественная гифка только если отправлять через SendVideo
                        /* Если передавать .setAnimation("http://www.meteoinfo.by/radar/UMMN/radar-map.gif")
                         * то приходит гифка за 2017 год ????
                         * поэтому такой костыль*/

                        execute(new SendVideo()
                                .setVideo(new File(
                                        MeteoradarUtil.getPathToGifFile(
                                                "http://www.meteoinfo.by/radar/UMMN/radar-map.gif")))
                                .setChatId(message.getChatId().toString()));
                    } catch (TelegramApiException e) {
                        sendMsg(message, "что-то пошло не так");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "скучно!":
                    try {
                        sendMsg(message, boredApi.getBoredStringFormat(boredModel));
                    } catch (IOException e) {
                        sendMsg(message, "что-то пошло не так");
                    }
                    break;
                case "курс валют":
                    try {
                        sendMsg(message, currency.getCurrency(currencyModel));
                    } catch (IOException e) {
                        sendMsg(message, "неизвестная ошибка");
                    }
                    break;

                default:
                    try {
                        sendMsg(message, Weather.getWeather(message.getText(), weatherModel));
                    } catch (IOException e) {
                        sendMsg(message, "такой город не найден");
                    }
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
        return setting.getUsername();
    }

    public String getBotToken() {
        return setting.getToken();
    }
}
