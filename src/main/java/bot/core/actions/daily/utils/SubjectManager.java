package bot.core.actions.daily.utils;

import bot.Bootstrapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class SubjectManager {

    public ArrayList<Subject> subjects = new ArrayList<Subject>();

    public SubjectManager() {
        if(!Bootstrapper.BotSettings.bot_properties.getProperty("path_to_days").equals("null")){
            try {
                initSubjects(
                        new FileInputStream(
                                new File(Bootstrapper.BotSettings.bot_properties.getProperty("path_to_days")
                                        + "/"
                                        + LocalDate.now().getDayOfWeek().toString().toLowerCase()
                                        + ".txt"
                        )));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else{
            initSubjects(
                    Objects.requireNonNull(
                            ClassLoader.getSystemResourceAsStream(
                                    "new_days/" + LocalDate.now().getDayOfWeek().toString().toLowerCase() + ".txt"
                            )));
        }
    }

    private void addSubject(Subject subject){
        subjects.add(subject);
    }
    private void addSubject(String line){
        subjects.add(new Subject(line));
    }

    public ArrayList<Subject> getSubjects() {
        return subjects;
    }

    private void initSubjects(InputStream inputStream){
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                addSubject(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String TodaySchedule() {
        String newLine = System.getProperty("line.separator");
        StringBuilder stringBuilder = new StringBuilder();
        int counter = 1;
        for(Subject subject: subjects){
            stringBuilder
                    .append(counter++ + ". ")
                    .append(subject)
                    .append(newLine);
        }
        return stringBuilder.toString();
    }
}
