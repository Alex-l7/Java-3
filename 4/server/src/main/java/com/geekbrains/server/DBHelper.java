package com.geekbrains.server;

import java.sql.*;

public class DBHelper implements AutoCloseable {
    private static DBHelper instance;
    private static Connection connection;

    private DBHelper() { // создаём приватный конструктор, чтобы никто из вне не мог создать экземрляр данного класса
    }

    public static DBHelper getInstance() { // метод getInstance() возвращает DBHelper()(Объект данного класса)
        if (instance == null) { // если instance ранее не был создан, то
            loadDriverAndOpenConnection(); // запускаем loadDriverAndOpenConnection()
            instance = new DBHelper(); // создаётся экземпляр данного класса
        }
        return instance;
    }

    private static void loadDriverAndOpenConnection() {
        try {
            Class.forName("org.sqlite.JDBC"); // запускаем драйвер
            connection = DriverManager.getConnection("jdbc:sqlite:secondDB.db"); // открываем соединение

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Ошибка открытия соединения с базой данных!");
            e.printStackTrace();
        }
    }

    public String findByLoginAndPassword(String login, String password) {
        //String query = String.format("SELECT * FROM client WHERE LOWER(login)=LOWER(\"%s\") AND password=\"%s\"", login, password);
        String query = String.format("SELECT name FROM client WHERE login=\"%s\" AND password=\"%s\"", login, password);
       // System.out.println(query);
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                //return resultSet.getString("nickname");
                return resultSet.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int updateNickname(String oldNickname, String newNickname) {
        String query = String.format("UPDATE client SET name=\"%s\" WHERE name=\"%s\"", newNickname, oldNickname);

        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(query); // возвращает колличество обновлённых строк

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
