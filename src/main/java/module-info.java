module org.chat.chatapplication {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.chat.chatapplication to javafx.fxml;
    exports org.chat.chatapplication;
}