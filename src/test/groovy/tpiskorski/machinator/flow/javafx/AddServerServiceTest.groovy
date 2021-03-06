package tpiskorski.machinator.flow.javafx

import spock.lang.Specification
import spock.lang.Subject
import tpiskorski.machinator.model.server.Server
import tpiskorski.machinator.model.server.ServerService
import tpiskorski.machinator.model.vm.VirtualMachine

class AddServerServiceTest extends Specification {

    def serverService = Mock(ServerService)

    @Subject addRemoteServerService = new AddServerService(
            serverService: serverService
    )

    def 'should create action that does server and vms update'() {
        given:
        def server = Mock(Server)
        def vms = [Mock(VirtualMachine)]
        def action = addRemoteServerService.addServerAndVmsAction(server, vms)

        when:
        action.perform()

        then:
        1 * serverService.add(server)
        1 * serverService.refresh(server, vms)
    }
}
