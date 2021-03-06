package tpiskorski.machinator.ui.controller.backup;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import tpiskorski.machinator.config.Config;
import tpiskorski.machinator.config.ConfigService;
import tpiskorski.machinator.model.backup.BackupDefinition;
import tpiskorski.machinator.model.backup.BackupDefinitionService;
import tpiskorski.machinator.ui.control.BackupTableRow;
import tpiskorski.machinator.ui.control.ConfirmationAlertFactory;
import tpiskorski.machinator.ui.core.ContextAwareSceneLoader;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@Controller
public class BackupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackupController.class);

    @Autowired private ConfigService configService;
    @Autowired private ContextAwareSceneLoader contextAwareSceneLoader;
    @Autowired private BackupDefinitionService backupDefinitionService;

    @FXML private TableView<BackupDefinition> backupsTableView;
    @FXML private Button removeVmButton;
    @FXML private TextField backupLocation;
    @FXML private ContextMenu contextMenu;
    @FXML private MenuItem dynamicMenuItem;
    @FXML private Button triggerNowButton;
    @FXML private Button dynamicButton;

    private Stage addServerStage;

    @FXML
    public void initialize() throws IOException {
        backupsTableView.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
            if (t.getButton() == MouseButton.SECONDARY) {
                BackupDefinition selectedItem = backupsTableView.getSelectionModel().getSelectedItem();
                if (selectedItem.isActive()) {
                    dynamicMenuItem.setText("Deactivate");
                    dynamicMenuItem.setOnAction(this::deactivate);
                } else {
                    dynamicMenuItem.setText("Activate");
                    dynamicMenuItem.setOnAction(this::activate);
                }

                contextMenu.show(backupsTableView, t.getScreenX(), t.getScreenY());
            }
        });

        dynamicButton.setOnAction(event -> {
            BackupDefinition selectedItem = backupsTableView.getSelectionModel().getSelectedItem();
            if (selectedItem.isActive()) {
                dynamicButton.setText("Deactivate");
                deactivate(event);
            } else {
                dynamicButton.setText("Activate");
                activate(event);
            }
        });

        backupsTableView.setRowFactory((tableview) -> new BackupTableRow());

        backupLocation.setText(configService.getConfig().getBackupLocation());
        configService.addPropertyChangeListener(evt -> {
            Config newValue = (Config) evt.getNewValue();
            backupLocation.setText(newValue.getBackupLocation());
        });

        removeVmButton.disableProperty().bind(Bindings.isEmpty(backupsTableView.getSelectionModel().getSelectedItems()));
        triggerNowButton.disableProperty().bind(Bindings.isEmpty(backupsTableView.getSelectionModel().getSelectedItems()));
        dynamicButton.disableProperty().bind(Bindings.isEmpty(backupsTableView.getSelectionModel().getSelectedItems()));

        addServerStage = contextAwareSceneLoader.loadPopup("/fxml/backup/addVmBackup.fxml");
        addServerStage.setTitle("Adding backup...");

        backupsTableView.setItems(backupDefinitionService.getBackups());
    }

    private void deactivate(ActionEvent actionEvent) {
        boolean confirmed = ConfirmationAlertFactory.createAndAsk(
            "Do you really want to deactivate this backup?",
            "Backup"
        );

        if (confirmed) {
            BackupDefinition backupToDeactivate = backupsTableView.getSelectionModel().getSelectedItem();
            backupDefinitionService.deactivate(backupToDeactivate);
        }
    }

    @FXML
    public void activate(ActionEvent actionEvent) {
        boolean confirmed = ConfirmationAlertFactory.createAndAsk(
            "Do you really want to activate this backup?",
            "Backup"
        );

        if (confirmed) {
            BackupDefinition backupToDeactivate = backupsTableView.getSelectionModel().getSelectedItem();
            backupDefinitionService.activate(backupToDeactivate);
        }
    }

    @FXML
    public void browseHomeDirectory() {
        String backupLocation = configService.getConfig().getBackupLocation();
        try {
            Desktop.getDesktop().open(new File(backupLocation));
        } catch (IOException e) {
            LOGGER.warn("Could not open directory {}", backupLocation);
        }
    }

    public void showAddVm() {
        if (addServerStage.isShowing()) {
            addServerStage.hide();
        } else {
            addServerStage.show();
        }
    }

    @FXML
    public void removeBackup() {
        boolean confirmed = ConfirmationAlertFactory.createAndAsk(
            "Do you really want to remove this backup?",
            "Backup"
        );

        if (confirmed) {
            BackupDefinition backupDefinitionToRemove = backupsTableView.getSelectionModel().getSelectedItem();
            backupDefinitionService.remove(backupDefinitionToRemove);
        }
    }

    @FXML
    public void triggerNow() {
        boolean confirmed = ConfirmationAlertFactory.createAndAsk(
            "Do you really want to trigger this backup now?",
            "Backup"
        );

        if (confirmed) {
            BackupDefinition backupDefinitionToTrigger = backupsTableView.getSelectionModel().getSelectedItem();
            backupDefinitionService.triggerNow(backupDefinitionToTrigger);
        }
    }
}
