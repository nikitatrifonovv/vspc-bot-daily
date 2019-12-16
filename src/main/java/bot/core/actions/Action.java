package bot.core.actions;

import bot.core.interfaces.Loggable;
import com.vk.api.sdk.objects.messages.Message;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public abstract class Action implements Loggable {

    public final String name;

    public Action(String name){
        this.name = name;
    }

    public abstract void execute(Message message);

    protected Message buildMessage(String text,int peerId){
        return new Message()
                .setText(text)
                .setPeerId(peerId)
                .setRandomId((int) System.currentTimeMillis());
    }

    protected String log(Message message){
        SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy zzz");
        date.setTimeZone(TimeZone.getTimeZone("GMT+4:00"));
        return "Action executed: "
                + "["
                + "name: " + this.getClass().getSimpleName()
                + " userId: " + message.getPeerId()
                + " time: " + date.format(System.currentTimeMillis())
                +"]";
    }
}
