package tpiskorski.machinator.quartz.server

import javafx.collections.ObservableList
import org.quartz.JobExecutionContext
import spock.lang.Specification
import spock.lang.Subject
import tpiskorski.machinator.core.server.Server
import tpiskorski.machinator.core.server.ServerService
import tpiskorski.machinator.core.server.ServerType

class ServerRefreshJobTest extends Specification {

    def serverRefreshService = Mock(ServerRefreshService)
    def serverService = Mock(ServerService)

    @Subject job = new ServerRefreshJob(serverRefreshService, serverService)

    def 'should not do server refresh job if there are no servers'() {
        given:
        def jobExecutionContext = Mock(JobExecutionContext)

        when:
        job.executeInternal(jobExecutionContext)

        then:
        1 * serverService.getServers() >> ([] as ObservableList)
        0 * serverRefreshService.monitor(_)
        0 * serverService.updateReachable(_, _)
    }

    def 'should not do server refresh job if servers are remote'() {
        given:
        def jobExecutionContext = Mock(JobExecutionContext)
        def server1 = Mock(Server) {
            getServerType() >> ServerType.REMOTE
        }
        def server2 = Mock(Server) {
            getServerType() >> ServerType.REMOTE
        }

        when:
        job.executeInternal(jobExecutionContext)

        then:
        1 * serverService.getServers() >> ([server1, server2] as ObservableList)
        0 * serverRefreshService.monitor(server1)
        0 * serverRefreshService.monitor(server2)
        0 * serverService.updateReachable(_, _)
    }

    def 'should refresh each server'() {
        given:
        def jobExecutionContext = Mock(JobExecutionContext)
        def server1 = Mock(Server) {
            getServerType() >> ServerType.LOCAL
        }
        def server2 = Mock(Server) {
            getServerType() >> ServerType.LOCAL
        }

        when:
        job.executeInternal(jobExecutionContext)

        then:
        1 * serverService.getServers() >> ([server1, server2] as ObservableList)
        1 * serverRefreshService.monitor(server1)
        1 * serverRefreshService.monitor(server2)
        2 * serverService.refresh(_, _)
    }
}
