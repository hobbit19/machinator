package tpiskorski.vboxcm.lifecycle.state.manager

import spock.lang.Specification
import tpiskorski.vboxcm.core.backup.BackupDefinition
import tpiskorski.vboxcm.core.server.Server
import tpiskorski.vboxcm.core.vm.VirtualMachine
import tpiskorski.vboxcm.lifecycle.state.serialize.model.SerializableBackupDefinition

class SerializableBackupDefinitionTest extends Specification {

    def 'should create serializable backup from backup and convert it back'() {
        given:
        def server = new Server('other', '321')
        def vm = new VirtualMachine(new Server('some', '123'), 'id1')
        def backup = new BackupDefinition(server, vm)

        when:
        def serializableBackup = new SerializableBackupDefinition(backup)

        and:
        def convertedBackBackup = serializableBackup.toBackup()

        then:
        convertedBackBackup.server == server
        convertedBackBackup.vm == vm
        convertedBackBackup == backup
    }
}