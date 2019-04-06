package tpiskorski.vboxcm.lifecycle.state.serialize.model;

import tpiskorski.vboxcm.core.backup.BackupDefinition;

import java.io.Serializable;

public class SerializableBackupDefinition implements Serializable {

    private static final long serialVersionUID = 1L;

    private SerializableServer serializableServer;
    private SerializableVirtualMachine serializableVirtualMachine;

    public SerializableBackupDefinition(BackupDefinition backupDefinition) {
        this.serializableServer = new SerializableServer(backupDefinition.getServer());
        this.serializableVirtualMachine = new SerializableVirtualMachine(backupDefinition.getVm());
    }

    public BackupDefinition toBackup() {
        return new BackupDefinition(serializableServer.toServer(), serializableVirtualMachine.toVirtualMachine());
    }
}