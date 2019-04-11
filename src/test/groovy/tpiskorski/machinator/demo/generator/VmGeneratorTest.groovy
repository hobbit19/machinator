package tpiskorski.machinator.demo.generator

import tpiskorski.machinator.core.server.Server
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class VmGeneratorTest extends Specification {

    @Subject generator = new VmGenerator()

    @Unroll
    def 'should generate vms'() {
        given:
        def server = Mock(Server) {
            getAddress() >> 'some:address'
        }

        expect:
        generator.generateVirtualMachines(server, toGenerate).size() == toGenerate

        where:
        toGenerate << [1, 3, 10]
    }
}