import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class Bot extends TelegramLongPollingBot
{
    private enum BotState {
        IDLE, WAITING_FOR_NUMBER
    }
    private BotState currentState = BotState.IDLE;
    private int value = 0;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText().trim();
            try
            {

                switch (currentState)
                {
                    case IDLE:
                        if (text.startsWith("/increment"))
                        {
                            currentState = BotState.WAITING_FOR_NUMBER;
                            execute(replyMessage(update, "Please enter a number to increment the value"));
                        }
                        if(text.startsWith("/clear"))
                        {

                        }
                        else
                            execute(replyMessage(update, "☺"));
                        break;
                    case WAITING_FOR_NUMBER:
                        int num = Integer.parseInt(text);
                        value += num;
                        currentState = BotState.IDLE;
                        execute(replyMessage(update, "Value incremented by " + num + " to " + "*" + value + "*"));
                        break;
                }
            }
            catch (NumberFormatException e)
            {
                try
                {
                    execute(replyMessage(update, "Invalid number, please try again"));
                } catch (TelegramApiException ex)
                {
                    throw new RuntimeException(ex);
                }
            } catch (TelegramApiException e)
            {
                e.printStackTrace();
            }
        }
    }

    private SendMessage replyMessage(Update update, String text) {
        var message = new SendMessage(update.getMessage().getChatId().toString(), text);
        message.setParseMode(ParseMode.MARKDOWN);
        return message;
    }

    @Override
    public String getBotUsername() {
        return "MySimpleBot";
    }

    @Override
    public String getBotToken() {
        return "TELEGRAM_TOKEN";
    }
}
