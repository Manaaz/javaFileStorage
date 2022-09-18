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

public class PrimaryController implements Initializable {

    File file = null;

    @FXML
    private Label label;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObjectRegistry.reg(this.getClass(), this);
    }

    public void uploadFile() {

        JFileChooser fileopen = new JFileChooser();
        int ret = fileopen.showDialog(null, "Открыть файл");
        if (ret == JFileChooser.APPROVE_OPTION) {
            file = fileopen.getSelectedFile();
        }

        if (file.isFile() && file.exists()) {
            UploadFileService uploadFileService = ObjectRegistry.getInstance(UploadFileService.class);
            uploadFileService.uploadFile(file.getPath());
        }
    }

    @FXML
    private void switchToSecondary() {
        //NettyClient nettyClient = ObjectRegistry.getInstance(NettyClient.class);
        //nettyClient.getFilesList();
    }

    public void setLabelText(String text) {
        label.setText(text + " " + new Date().toString());
    }
}
