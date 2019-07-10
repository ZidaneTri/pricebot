package com.example.pricebot;

import com.example.pricebot.entity.Client;
import com.example.pricebot.entity.Product;
import com.example.pricebot.entity.Request;

import com.example.pricebot.entity.Shop;
import com.example.pricebot.entity.key.RequestId;
import com.example.pricebot.exception.ProductNotFoundException;
import com.example.pricebot.exception.ShopsNotFoundException;
import com.example.pricebot.repository.ClientRepository;
import com.example.pricebot.service.ClientService;
import com.example.pricebot.service.ProductService;
import com.example.pricebot.service.RequestService;
import com.example.pricebot.service.ShopService;
import com.example.pricebot.util.ParseUtil;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Component
public class OnlinerBot extends TelegramLongPollingBot {

    private final int NO_STATE = 0;
    private final int ADD_WAIT_PRODUCT = 1;
    private final int ADD_WAIT_SHOP = 2;

    private final
    RequestService requestService;

    private final
    ProductService productService;

    private final
    ShopService shopService;

    private final
    ClientService clientService;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    public OnlinerBot(ClientService clientService, ShopService shopService, RequestService requestService, ProductService productService){
        this.clientService = clientService;
        this.shopService = shopService;
        this.requestService = requestService;
        this.productService = productService;
    }

    //TODO: вынести строки в property
    @Override
    public void onUpdateReceived(Update update) {

        Message currentMessage;
        CallbackQuery query;

        if (update.hasMessage()) {

            currentMessage = update.getMessage();

            if (currentMessage.getText().startsWith("/")) {
                //обработка команд
                switch (currentMessage.getText()){
                    case "/start":
                        startCommandHandler(currentMessage);
                        break;
                    //TODO: остальные возможные команды
                    default:
                        break;
                }
            } else if(checkStates(currentMessage.getChatId())) {
                //обработка состояний
                Client currentClient = clientService.getByChatId(currentMessage.getChatId());
                if(currentClient.getAddState() != 0) {
                    switch (currentClient.getAddState()) {
                        case ADD_WAIT_PRODUCT:
                            addProductHandler(currentMessage);
                            break;
                        default:
                            break;
                    }

                }
            } else {
                //обработка кнопок главного меню
                switch (currentMessage.getText()){
                    case "Добавить":
                        addButtonHandler(currentMessage);
                        break;
                    //TODO: остальные возможные кнопки
                    default:
                        break;
                }
            }
        } else if (update.hasCallbackQuery()){
            //обработка callback'ов от инлайн-клавиатур
            query = update.getCallbackQuery();
            Client currentClient = clientService.getByChatId(query.getMessage().getChatId());
            if(query.getData().startsWith("DROPADD")){
                dropAddButtonHandler(query);
            } else if (query.getData().startsWith("SHOP")){
                addShopHandler(query);
            }

        } else {
            currentMessage = update.getMessage();
            try {
                execute(new SendMessage().setChatId(String.valueOf(currentMessage.getChatId()))
                        .setText("Извините, но я не понимаю, что вы мне отправили."));

            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void startCommandHandler(Message message){

        if(!clientService.existsByChatId(message.getChatId())){
            Client currentClient = new Client (
                    message.getChatId(),
                    message.getFrom().getFirstName(),
                    message.getFrom().getLastName(),
                    message.getFrom().getUserName()
            );
            clientService.saveClient(currentClient);

            send(new SendMessage().setChatId(String.valueOf(message.getChatId()))
                    .setText("Добро пожаловать!").setReplyMarkup(baseKeyboard()));
        } else {
            send(new SendMessage().setChatId(String.valueOf(message.getChatId()))
                    .setText("Вы уже стартовали бота. Нет необходимости делать это еще раз :)"));
        }
    }

    public void addButtonHandler(Message message){
        Client currentClient = clientService.getByChatId(message.getChatId());

        currentClient.setAddState(ADD_WAIT_PRODUCT);
        clientService.saveClient(currentClient);

        send(new SendMessage().setChatId(message.getChatId())
                 .setText("Скормите мне ссылку с товаром.").setReplyMarkup(addProductKeyboard()));
    }

    public void dropAddButtonHandler(CallbackQuery query){
        Client currentClient = clientService.getByChatId(query.getMessage().getChatId());

        send(new SendMessage().setChatId(query.getMessage().getChatId())
                .setText("Ваш запрос сброшен.").setReplyMarkup(baseKeyboard()));
        send(new EditMessageReplyMarkup().setChatId(query.getMessage().getChatId()).setMessageId(query.getMessage().getMessageId()).setReplyMarkup(null));


        currentClient.setAddState(NO_STATE);
        currentClient.setCurrentProduct(null);
        clientService.saveClient(currentClient);

    }

    public void addProductHandler(Message message){

        if(message.getText().startsWith("https://catalog.onliner.by/")) {

            //код продукта - последний элемент в массиве строк
            String[] split = message.getText().split("/");
            String productCode = split[split.length-1];

            //получаем данные о продукте
            Product product;
            if(!productService.existsByCode(productCode)) {
                try{
                    product = ParseUtil.parseProduct(productCode);
                } catch (ProductNotFoundException e){
                    send(new SendMessage().setChatId(message.getChatId())
                            .setText("Такого товара не существует в каталоге. Пожалуйста, попробуйте добавить продукт заново."));
                    return;
                } catch (IOException e){
                    send(new SendMessage().setChatId(message.getChatId())
                            .setText("Произошла ошибка во время получения данных. Пожалуйста, попробуйте добавить продукт заново."));
                    return;
                }
            } else {
                product = productService.getByCode(productCode);
            }
            //получаем данные о магазинах
            Set<Shop> shops;
            try{
                shops = ParseUtil.parseShops(productCode);
            } catch (ShopsNotFoundException e){
                send(new SendMessage().setChatId(message.getChatId())
                        .setText("Товар не продаётся ни в одном магазине. Пожалуйста, добавьте другой продукт"));
                return;
            } catch (IOException e){
                send(new SendMessage().setChatId(message.getChatId())
                        .setText("Произошла ошибка во время получения данных. Пожалуйста, попробуйте добавить продукт заново."));
                return;
            }
            product = productService.saveProduct(product);
            shops = shopService.saveAll(shops);
            Client currentClient = clientService.getByChatId(message.getChatId());
            currentClient.setCurrentProduct(product.getCode());
            currentClient.setAddState(ADD_WAIT_SHOP);
            clientService.saveClient(currentClient);

            send(new SendMessage().setChatId(message.getChatId()).enableMarkdown(true)
                    .setText("Итак, вы хотите мониторить цену на [" + product.getName() + "](" + product.getLink() + "). Теперь выберите магазин.")
                    .setReplyMarkup(shopSelectKeyboard(shops)));
        } else{
            send(new SendMessage().setChatId(message.getChatId())
                    .setText("Ой, я не смог понять то, что вы мне отправили. Это точно ссылка на товар в каталоге?"));
        }
    }

    @Transactional
    public void addShopHandler(CallbackQuery query) {
        Client currentClient = clientService.getByChatId(query.getMessage().getChatId());
        String[] split = query.getData().split("_");
        Shop shop = shopService.getByCode(split[split.length-1]);
        requestService.saveRequest(new Request(new RequestId(
                currentClient,
                productService.getByCode(currentClient.getCurrentProduct()),
                shop
        )));
        currentClient.setAddState(NO_STATE);
        currentClient.setCurrentProduct(null);
        clientService.saveClient(currentClient);

        send(new AnswerCallbackQuery().setCallbackQueryId(query.getId()).setText("Добавлено"));
        send(new DeleteMessage().setChatId(query.getMessage().getChatId()).setMessageId(query.getMessage().getMessageId()));
        send(new SendMessage().setChatId(query.getMessage().getChatId())
                .setText("Отлично! Ваш запрос добавлен и теперь каждый день вы будете получать цену на этот товар!").setReplyMarkup(baseKeyboard()));
    }

    private static InlineKeyboardMarkup shopSelectKeyboard(Set<Shop> shops){

        List<List<InlineKeyboardButton>> shopKeyboard  = new ArrayList<>();
        List<InlineKeyboardButton> inlineKeyboardRow = new ArrayList<>();
        int rowCounter = 0;
        //выстраиваем кнопки в три колонки, поэтому добавляем в лист листов внутри цикла
        for (Shop shop : shops) {
            if (rowCounter == 3) {
                shopKeyboard.add(inlineKeyboardRow);
                inlineKeyboardRow = new ArrayList<>();
                rowCounter = 0;
            }
            inlineKeyboardRow.add(new InlineKeyboardButton().setText(shop.getName()).setCallbackData("SHOP_" + shop.getCode()));
            rowCounter++;
        }
        shopKeyboard.add(inlineKeyboardRow);
        List<InlineKeyboardButton> inlineKeyboardDropButtonRow = new ArrayList<>();
        inlineKeyboardDropButtonRow.add(new InlineKeyboardButton().setText("Сбросить").setCallbackData("DROPADD"));
        shopKeyboard.add(inlineKeyboardDropButtonRow);

        return new InlineKeyboardMarkup().setKeyboard(shopKeyboard);
    }

    private static ReplyKeyboardMarkup baseKeyboard(){

        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow addRow = new KeyboardRow();
        addRow.add(new KeyboardButton().setText("Добавить"));
        KeyboardRow updateRow = new KeyboardRow();
        updateRow.add(new KeyboardButton().setText("Список запросов"));
        rows.add(addRow);
        rows.add(updateRow);
        return new ReplyKeyboardMarkup().setKeyboard(rows).setResizeKeyboard(true).setOneTimeKeyboard(true);
    }

    private static InlineKeyboardMarkup addProductKeyboard(){

        List<List<InlineKeyboardButton>> dropKeyboard  = new ArrayList<>();
        List<InlineKeyboardButton> inlineKeyboardRow = new ArrayList<>();
        inlineKeyboardRow.add(new InlineKeyboardButton().setText("Сбросить").setCallbackData("DROPADD"));
        dropKeyboard.add(inlineKeyboardRow);

        return new InlineKeyboardMarkup().setKeyboard(dropKeyboard);
    }

    private boolean checkStates(Long chatId){
        if(clientService.existsByChatId(chatId)){
            Client currentClient = clientService.getByChatId(chatId);
            return currentClient.getAddState() > 0;
        } else return false;

    }

    private void send(BotApiMethod action){
        try {
            execute(action);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "botName";
    }


    @Override
    public String getBotToken() {
        return "botToken";
    }

}