package groovy.com.mg.persistence.services.integration


import de.flapdoodle.embed.mongo.MongodExecutable
import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.IMongodConfig
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder
import de.flapdoodle.embed.mongo.config.Net
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.runtime.Network
import spock.lang.Specification

class IntegrationTestsSetup extends Specification {


    private static MongodExecutable mongoExecutable = null


    def setupSpec() {
        startEmbeddedMongo()

    }


    def cleanupSpec() {
        stopEmbeddedMongo()
    }

    private static void startEmbeddedMongo() {
        MongodStarter starter = MongodStarter.getDefaultInstance()
        String bindIp = "localhost"
        int port = 12345
        IMongodConfig mongoConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION).net(new Net(bindIp, port, Network.localhostIsIPv6())).build()

        mongoExecutable = null
        try {
            mongoExecutable = starter.prepare(mongoConfig)
            mongoExecutable.start()
        } catch (Exception e) {
            // log exception here
            if (mongoExecutable != null)
                mongoExecutable.stop()
        }

    }


    private static void stopEmbeddedMongo() {
        if (mongoExecutable != null) {
            mongoExecutable.stop()
        }
    }
}
