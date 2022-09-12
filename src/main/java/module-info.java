module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.netty.transport;
    requires io.netty.codec;
    requires io.netty.buffer;

    exports ru.gb.file.warehouse;
    opens ru.gb.file.warehouse to javafx.fxml;
}
