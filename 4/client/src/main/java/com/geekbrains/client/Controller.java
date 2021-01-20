package com.geekbrains.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    TextArea textArea;

    @FXML
    TextField msgField, loginField;

    @FXML
    HBox msgPanel, authPanel;

    @FXML
    PasswordField passField;

    @FXML
    ListView<String> clientsList;

    private boolean authenticated;
    private String nickname;
    private String login; // добавляем поле логин

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
        authPanel.setVisible(!authenticated);
        authPanel.setManaged(!authenticated);
        msgPanel.setVisible(authenticated);
        msgPanel.setManaged(authenticated);
        clientsList.setVisible(authenticated);
        clientsList.setManaged(authenticated);

        if (!authenticated) {
            nickname = "";
            login = ""; //  login = "" работает так же, как и nickname = ""
        } else {
            String history = History.getLastLinesOfHistory(login); // когда вызван метод setAuthenticated и передано true (authenticated),  то мы сразу пытаемся запрсить из фала последние строки  из нашей истории
            textArea.appendText(history); // выводим нашу историю в текстовое поле
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setAuthenticated(false);
        clientsList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String nickname = clientsList.getSelectionModel().getSelectedItem();
                msgField.setText("/w " + nickname + " ");
                msgField.requestFocus();
                msgField.selectEnd();
            }
        });
        linkCallbacks();
    }

    public void sendAuth() {
        login = loginField.getText(); // записываем логин в метод

        Network.sendAuth(loginField.getText(), passField.getText());
        loginField.clear();
        passField.clear();
    }

    public void sendMsg() {
        if (Network.sendMsg(msgField.getText())) {
            msgField.clear();
            msgField.requestFocus();
        }
    }

    public void showAlert(String msg) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
            alert.showAndWait();
        });
    }

    public void linkCallbacks() {
        Network.setCallOnException(args -> showAlert(args[0].toString()));

        Network.setCallOnCloseConnection(args -> setAuthenticated(false));

        Network.setCallOnAuthenticated(args -> {
            setAuthenticated(true);
            nickname = args[0].toString();
        });

        Network.setCallOnMsgReceived(args -> {
            String msg = args[0].toString();
            if (msg.startsWith("/")) {
                if (msg.startsWith("/clients ")) {
                    String[] tokens = msg.split("\\s");
                    Platform.runLater(() -> {
                        clientsList.getItems().clear();
                        for (int i = 1; i < tokens.length; i++) {
                            clientsList.getItems().add(tokens[i]);
                        }
                    });
                }
            } else {
                String message = msg + System.lineSeparator();
                textArea.appendText(message); // такой вариант написания кода более оптимален
//                textArea.appendText(msg + System.lineSeparator()); // сообщение выводится
               History.saveMessage(login, message); // сообщение сохраняется разделяя его с помощью lineSeparator()
            }
        });
    }
}