package tgbot.useradd.registr.KBOtgbotsecurity.bot;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tgbot.useradd.registr.KBOtgbotsecurity.entity.Users;
import tgbot.useradd.registr.KBOtgbotsecurity.repository.UserRepository;

@Component
public class MyTelegramBot extends TelegramLongPollingBot {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long telegramId = update.getMessage().getFrom().getId();
            String username = update.getMessage().getFrom().getUserName();
            String firstName = update.getMessage().getFrom().getFirstName();
            String lastName = update.getMessage().getFrom().getLastName();

            Users user = userRepository.findByTelegramId(telegramId).orElse(null);
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());

            if (user == null) {
                user = new Users();
                user.setTelegramId(telegramId);
                user.setUsername(username);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                userRepository.save(user);

                message.setText("Welcome! Please enter your city:");
            } else {
                message.setText("Welcome back, " + user.getFirstName() + "! Your city is: " + user.getCity());
            }

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasMessage() && userRepository.findByTelegramId(update.getMessage().getFrom().getId()).isPresent()) {
            Users user = userRepository.findByTelegramId(update.getMessage().getFrom().getId()).get();
            user.setCity(update.getMessage().getText());
            userRepository.save(user);

            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText("Thank you! Your city has been saved.");

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "BotSecurityTestBot";
    }

    @Override
    public String getBotToken() {
        return "7223594582:AAGSHs_AqmDkuNSEdHfnzbBijQkPqrq81IU";
    }
}