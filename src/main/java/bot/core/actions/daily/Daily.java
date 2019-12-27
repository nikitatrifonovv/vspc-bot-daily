package bot.core.actions.daily;

import bot.Bootstrapper;
import bot.core.actions.Action;
import bot.core.actions.daily.utils.SubjectManager;
import bot.vkcore.VKManager;
import com.vk.api.sdk.objects.messages.Message;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class Daily extends Action {
    private String string_to_format;
    private SubjectManager subjectManager;

    public Daily(ArrayList<String> word_tags) {
        super(word_tags);
        init();
    }
    private void init(){
        subjectManager = new SubjectManager();
        try {
            string_to_format = Bootstrapper.BotSettings.bot_properties.getProperty("path_to_daily").equals("null")?
                    ReadDailyText(Objects.requireNonNull(
                            ClassLoader.getSystemResourceAsStream(
                                    "new_days/" + LocalDate.now().getDayOfWeek().toString().toLowerCase() + ".txt"
                            )))
                    :
                    ReadDailyText(
                            new FileInputStream(
                                    new File(Bootstrapper.BotSettings.bot_properties.getProperty("path_to_daily")
                                            + "/"
                                            + "Daily_text.txt"
                                    ))
                    );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execute(Message message) {
        new VKManager().sendMessage(buildMessage(BuildRequestString(),message.getPeerId()));
        LogToConsole(log(message));
    }

    private String ReadDailyText(InputStream inputStream){
        String newLine = System.getProperty("line.separator");
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append(newLine);
            }
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String BuildRequestString(){
        String s = string_to_format;
        s = s.replace("count", Integer.toString(subjectManager.getSubjects().size()));
        s = s.replace("start_time",subjectManager.subjects.get(0).start_time);
        s = s.replace("end_time",subjectManager.subjects.get(subjectManager.subjects.size() - 1).end_time);
        s = s.replace("TodaySchedule",subjectManager.TodaySchedule());
        return s;
    }
}
