package com.geekbrains.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class History {

    private static final int COUNT_LAST_LINES = 100; // константа, колличество последних строк, которые будем читать из нашего файла. Изменяя это значение не залезая в код,изменяем поведение программы

    public static void saveMessage(String login, String message) { // метод сохраняет сообщения, и при сохранении соббщения передаёт login
        try {
            Files.write(getHistoryFilePath(login), message.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND); // в метод write передаём путь до нашего файла (getHistoryFilePath(login)),далее берём байты в нашем message (message.getBytes()), добавляем 2 параметра StandardOpenOption.CREATE, StandardOpenOption.APPEND
            // параметр StandardOpenOption.CREATE создаёт файл, если он ещё не создан
            // параметр StandardOpenOption.APPEND позволяет дописать файл
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Path getHistoryFilePath(String login) {
        Path historyPath = Paths.get("history", "history_" + login + ".txt"); // метод берёт путь history, файл history_логин.txt
        //  Paths.get позволяет вытащить путь со слешами history/history_логин.txt
        if (Files.notExists(historyPath.getParent())) { // проверяем через Files если у нас не существует (через notExists) родитель (getParent), т.е наша директория history,
            createHistoryDirectory(historyPath); // то мы нашу директорию должны создать
        }

        return historyPath;
    }

    private static void createHistoryDirectory(Path path) { // метод создания дирректории
        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getLastLinesOfHistory(String login) { // метод получения строк истории
        Path historyFilePath = getHistoryFilePath(login); // получается файл

        if (Files.notExists(historyFilePath))
            return ""; // если файл не существует (Files.notExists(historyFilePath)), возвращаем пустую строку ""

        try { // try catch-ем минимизируем кучу кода, и создаём метод getLastLinesOfHistory
            List<String> lines = Files.readAllLines(historyFilePath, StandardCharsets.UTF_8); // иначе читаем все строки в этом файле с заданной кодировкой (StandardCharsets.UTF_8)
            return getLastLines(lines); // и передаём эти строки методу getLastLines
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    private static String getLastLines(List<String> lines) {
        StringBuilder result = new StringBuilder(); // используем StringBuilder для создания строчки, добавляю какую-либо переменную и кучу других строчек, и не создавая постоянно новые объекты строк

        int startPos = 0; // создаём стартовую позицию
        if (lines.size() > COUNT_LAST_LINES) { // если колличество строк в файле истории (lines.size()) > 100,
            startPos = lines.size() - COUNT_LAST_LINES; // то мы должны от общего числа строк (lines.size()) отнять нужное нам значение (COUNT_LAST_LINES)
            // если у нас 500 строк, то startPos = 500 - 100
        }

        for (int i = startPos; i < lines.size(); i++) { // далее начинаем с 400 строчки и идём до конца до 500 строчки
            result.append(lines.get(i)).append(System.lineSeparator()); // добавляя строчку (lines.get(i)) в StringBuilder и добавляя lineSeparator() с помощью append(System.lineSeparator())
            // lineSeparator() это "/r/n" перенос строки и перенос каретки.Используется для разделения строк
        }

        return result.toString(); // возвращаем строчку

    }

}