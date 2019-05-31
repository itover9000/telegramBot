import model.BoredModel;
import model.CurrencyModel;
import model.GeomagneticStormModel;
import model.WeatherModel;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import service.BoredApi;
import service.Currency;
import service.GeomagneticStorm;
import service.Weather;
import util.GeomagneticStormUtil;
import util.MeteoradarUtil;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }

        //проверка бури с индексом > 4 каждые 3 часа и отправка ссобщения на эл. почту
        GeomagneticStormUtil.checkStormEvery3Hour(new GeomagneticStormModel());


    }

    public void onUpdateReceived(Update update) {
        WeatherModel weatherModel = new WeatherModel();
        CurrencyModel currencyModel = new CurrencyModel();
        BoredModel boredModel = new BoredModel();
        GeomagneticStormModel stormModel = new GeomagneticStormModel();
        Message message = update.getMessage();
        GeomagneticStorm geomagneticStorm = new GeomagneticStorm();

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
                        sendMsg(message, "состояние на " +  MeteoradarUtil.getTimeFromSite());
                    } catch (TelegramApiException | IOException | ParseException e) {
                        sendMsg(message, "что-то пошло не так");
                    }
                    break;

                case "анимация погоды":
                    try {
                        //пересылаю анимацию о погоде за последние 3 часа
                       /* Если передавать .setAnimation("http://www.meteoinfo.by/radar/UMMN/radar-map.gif")
                       * то приходит гифка за 2017 год ????
                       * поэтому такой костыль*/

                        execute(new SendAnimation()
                                .setAnimation(new File(
                                        MeteoradarUtil.getPathToGifFile(
                                                "http://www.meteoinfo.by/radar/UMMN/radar-map.gif")))
//                                .setAnimation("http://www.meteoinfo.by/radar/UMMN/radar-map.gif")
                                .setChatId(message.getChatId().toString()));
                    } catch (TelegramApiException e) {
                        sendMsg(message, "что-то пошло не так");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "скучно!":
                    try {
                        sendMsg(message, BoredApi.getBored(boredModel));
                    } catch (IOException e) {
                        sendMsg(message, "что-то пошло не так");
                    }
                    break;
                case "курс валют":
                    try {
                        sendMsg(message, Currency.getCurrency(currencyModel));
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
        return "itover9000_bot";
    }

    public String getBotToken() {
        return "884639123:AAGJUFOWF9hoyk4_qM7vNw3YFYHXpsxVAMA";
    }
}
