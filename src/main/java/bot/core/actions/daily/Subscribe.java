package bot.core.actions.daily;

import bot.Bootstrapper;
import bot.core.actions.base.Action;
import bot.core.vk.VKManager;
import com.vk.api.sdk.objects.messages.Message;

import java.util.ArrayList;

public class Subscribe extends Action {
    public Subscribe(ArrayList<String> word_tags) {
        super(word_tags);
    }

    @Override
    public void execute(Message message) {
        if(Bootstrapper.Configurations.getSubscribers().contains(message.getPeerId())){
            new VKManager().sendMessage(buildMessage("Вы уже подписаны на рассылку. Для отписки напишите \"Отписаться\"",message.getPeerId()));
        }
        else {
            Bootstrapper.Configurations.addSubscriber(message.getPeerId());
            new VKManager().sendMessage(buildMessage("Вы успешно подписались на рассылку!",message.getPeerId()));
        }
        LogToConsole(log(message));
    }
}
