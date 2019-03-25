package tpiskorski.vboxcm.ui.controller;

import tpiskorski.vboxcm.config.ConfigService;
import tpiskorski.vboxcm.stub.dynamic.ServerStubMonitor;
import tpiskorski.vboxcm.ui.core.ContextAwareSceneLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class FileMenuController {

    private final ContextAwareSceneLoader contextAwareSceneLoader;
    private final ConfigurableApplicationContext springContext;
    private final ConfigService configService;

    private final ServerStubMonitor serverStubMonitor;

    @FXML private MenuItem monitoringMenuItem;

    @FXML private Alert reloadAlert;
    @FXML private Alert monitorAlert;

    private Stage containerWindow;

    @Autowired
    public FileMenuController(ConfigService configService, ContextAwareSceneLoader contextAwareSceneLoader, ConfigurableApplicationContext springContext, ServerStubMonitor serverStubMonitor) {
        this.configService = configService;
        this.contextAwareSceneLoader = contextAwareSceneLoader;
        this.springContext = springContext;
        this.serverStubMonitor = serverStubMonitor;
    }

    @FXML
    public void initialize() throws IOException {
        containerWindow = contextAwareSceneLoader.loadPopup("/fxml/menu/config/config/baseConfigContainer.fxml");
        monitoringMenuItem.setText(serverStubMonitor.getIsFreezed().get() ? "Start Monitoring" : "Stop Monitoring");
    }

    @FXML
    public void reloadConfig() {
        configService.reload();
        reloadAlert.showAndWait();
    }

    @FXML
    public void configWindow() {
        if (containerWindow.isShowing()) {
            containerWindow.hide();
        } else {
            containerWindow.show();
        }
    }

    @FXML
    public void exit() {
        springContext.close();
        Platform.exit();
    }

    @FXML
    public void freezeMonitoring() {
        serverStubMonitor.freeze();
        monitoringMenuItem.setText(serverStubMonitor.getIsFreezed().get() ? "Start Monitoring" : "Stop Monitoring");
        monitorAlert.setContentText("Monitoring: " + !serverStubMonitor.getIsFreezed().get());
        monitorAlert.showAndWait();
    }
}