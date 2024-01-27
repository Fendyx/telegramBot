package io.proj3ct.demo.service;

import io.proj3ct.demo.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;
    static final String HELP_TEXT = "Type /start to start your work with me\n\n" +
            "Type /help to see help text messages\n\n" +
            "Type /info to get information about this bot and why it is usefull";
    static final String INFOTEXT = "Цей бот створений з метою допомоги учням та батькам дізнаватися корисну інформацію, таку як: Розклад уроків, розклад дзвінків, зв'язок с класним керівником, список вчителів. Потребуєте список команд? Натисніть /help. Інші питання - писати на пошту: andrey.knapp@gmail.com. Приємного користування ;)";
    static final String RINGS = "1) 8:05 - 8:50\n\n" +
            "2) 9:00 - 9:45\n\n" +
            "3) 9:55 - 10:40\n\n" +
            "4) 10:50 - 11:40\n\n" +
            "5) 11:50 - 12:35\n\n" +
            "6) 12:45 - 13:30\n\n" +
            "7) 13:40 - 14:25\n\n" +
            "8) 14:35 - 15:20";
    static final String CONTACT = "Жицький Юрій Федорович\nНомер телефону: +380681033384";
    static final String MONDAY = "1) Англійська мова\n" +
            "2) Географія\n" +
            "3) Громадянська освіта\n" +
            "4) Іспанська мова\n" +
            "5) Фізична культура\n" +
            "6) Фізика\n" +
            "7) Біологія і екологія";
    static final String TUESDAY = "1) Інформатика\n" +
            "2) Англійська мова\n" +
            "3) Алгебра\n" +
            "4) Українська мова\n" +
            "5) Географія\n" +
            "6) Всесвітня історія\n" +
            "7) Історія України\n" +
            "8) Фізика";
    static final String WEDNESDAY = "1) Біологія і екологія\n" +
            "2) Історія України\n" +
            "3) Англійська мова\n" +
            "4) Українська література\n" +
            "5) Геометрія\n" +
            "6) Фізична культура\n" +
            "7) Інформатика";
    static final String THURSDAY = "1) Фізика\n" +
            "2) Англійська мова\n" +
            "3) Хімія\n" +
            "4) Громадянська освіта\n" +
            "5) Українська мова\n" +
            "6) Фізична культура\n" +
            "7) Іспанська мова";
    static final String FRIDAY = "1) Іспанська мова\n" +
            "2) Англійська мова\n" +
            "3) Алгебра\n" +
            "4) Захист України\n" +
            "5) Зарубіжна література\n" +
            "6) Мистецтво\n" +
            "7) Українська література";
    static final String TEACHERS = "Англійська мова: Супрун Тетяна Володимирівна / Бунякіна Наталя Анатоліївна\n\n" +
            "Алгебра та геометрія: Полієвікова Тетяна Павлівна\n\n" +
            "Громадянська освіта та Історія України: Козлюк Алла Борисівна\n\n" +
            "Українська мова та література: Левінська Ірина Миколаївна\n\n" +
            "Фізика: Моргунова Аліна Андріївна\n\n" +
            "Хімія: Зима Галина Миколаївна\n\n" +
            "Іспанська мова: Сандецька Ірина Федорівна\n\n" +
            "Фізична культура: Левицький Вадим Юрійович / Головач Ольга Миколаївна\n\n" +
            "Захист України/Медицина: Нікітін Владислав Аркадійович / Атаманова Олена Юріївна\n\n" +
            "Мистецтво та Зарубіжна література: Битко Людмила Дмитрівна\n\n" +
            "Інформатика: Жицький Юрій Федорович / \n\n" +
            "Географія: Сергата Лариса Миколаївна\n\n" +
            "Біологія: Атаманова Олена Юріївна";



    public TelegramBot(BotConfig config){
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "get started"));
        listOfCommands.add(new BotCommand("/info", "info about bot"));
        listOfCommands.add(new BotCommand("/help", "how to work with me"));
        try{
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        }catch (TelegramApiException e){

        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }
    public String getBotToken(){
        return config.getToken();
    }
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch(messageText){
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    sendMessageWithKeyboard(chatId, "Оберіть що вас цікавить");
                    break;
                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    break;
                case "/info":
                    sendMessage(chatId, INFOTEXT);
                    break;
                case "Розклад уроків":
                    sendScheduleButtons(chatId);
                    break;
                case "Розклад дзвінків":
                    sendMessage(chatId, RINGS);
                    break;
                case "Зв'язок з класним керівником":
                    sendMessage(chatId, CONTACT);
                    break;
                case "Вчителі":
                    sendMessage(chatId, TEACHERS);
                    break;
                case "Понеділок":
                    sendMessage(chatId, MONDAY);
                    break;
                case "Вівторок":
                    sendMessage(chatId, TUESDAY);
                    break;
                case "Середа":
                    sendMessage(chatId, WEDNESDAY);
                    break;
                case "Четвер":
                    sendMessage(chatId, THURSDAY);
                    break;
                case "П'ятниця":
                    sendMessage(chatId, FRIDAY);
                    break;
                case "Назад":
                    sendMessageWithKeyboard(chatId, "Оберіть що вас цікавить");
                    break;

                default: sendMessage(chatId, "sorry, there is no such a command");
            }

        }
    }
    private void startCommandReceived(long chatId, String name){
        String answer = "Вітаю, " + name + " я допоможу вам дізнатися що вас цікавить.";
        sendMessage(chatId, answer);

    }
    private void sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try{
            execute(message);
        }catch (TelegramApiException e){

        }
    }
    private void sendMessageWithKeyboard(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        // Create keyboard
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        // First row of buttons
        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();
        KeyboardRow keyboardRow3 = new KeyboardRow();
        KeyboardRow keyboardRow4 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("Розклад уроків"));
        keyboardRow2.add(new KeyboardButton("Розклад дзвінків"));
        keyboardRow3.add(new KeyboardButton("Зв'язок з класним керівником"));
        keyboardRow4.add(new KeyboardButton("Вчителі"));


        // Add rows to the keyboard
        keyboardRows.add(keyboardRow1);
        keyboardRows.add(keyboardRow2);
        keyboardRows.add(keyboardRow3);
        keyboardRows.add(keyboardRow4);

        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendScheduleButtons(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Оберіть день тижня");

        // Создайте клавиатуру
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();


        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();
        KeyboardRow keyboardRow3 = new KeyboardRow();
        KeyboardRow keyboardRow4 = new KeyboardRow();
        KeyboardRow keyboardRow5= new KeyboardRow();
        KeyboardRow keyboardRow6 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("Назад"));
        keyboardRow2.add(new KeyboardButton("Понеділок"));
        keyboardRow3.add(new KeyboardButton("Вівторок"));
        keyboardRow4.add(new KeyboardButton("Середа"));
        keyboardRow5.add(new KeyboardButton("Четвер"));
        keyboardRow6.add(new KeyboardButton("П'ятниця"));

        keyboardRows.add(keyboardRow1);
        keyboardRows.add(keyboardRow2);
        keyboardRows.add(keyboardRow3);
        keyboardRows.add(keyboardRow4);
        keyboardRows.add(keyboardRow5);
        keyboardRows.add(keyboardRow6);

        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
