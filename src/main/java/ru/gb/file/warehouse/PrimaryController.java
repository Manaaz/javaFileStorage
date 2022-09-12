package ru.gb.file.warehouse;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import ru.gb.file.warehouse.netty.client.NettyClient;
import ru.gb.file.warehouse.netty.client.service.UploadFileService;

import java.net.URL;
import java.util.ResourceBundle;

public class PrimaryController implements Initializable {

    @FXML
    private Label label;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObjectRegistry.reg(this.getClass(), this);
    }

    public void uploadFile() {
        UploadFileService uploadFileService = ObjectRegistry.getInstance(UploadFileService.class);
        uploadFileService.uploadFile("/Users/bchervoniy/IdeaProjects/file-warehouse/client-dir/img.png");
    }

    @FXML
    private void switchToSecondary() {
        NettyClient nettyClient = ObjectRegistry.getInstance(NettyClient.class);
        nettyClient.getFilesList();
    }

    public void setLabelText(String text) {
        label.setText(text);
    }
}
