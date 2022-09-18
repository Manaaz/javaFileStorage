package ru.gb.file.warehouse;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import ru.gb.file.warehouse.netty.client.NettyClient;
import ru.gb.file.warehouse.netty.client.service.UploadFileService;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class AuthorizationFormController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObjectRegistry.reg(this.getClass(), this);
    }


}
