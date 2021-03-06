package tpiskorski.machinator.model.backup;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Repository;

@Repository
public class BackupDefinitionRepository {

    private final ObservableList<BackupDefinition> jobObservableList = FXCollections.observableArrayList(BackupDefinition.extractor());

    void add(BackupDefinition backupDefinition) {
        jobObservableList.add(backupDefinition);
    }

    ObservableList<BackupDefinition> getBackups() {
        return jobObservableList;
    }

    void remove(BackupDefinition backupDefinition) {
        jobObservableList.remove(backupDefinition);
    }
}
