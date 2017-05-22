package nl.choutman.allinn.koos;

import com.hazelcast.config.Config;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import nl.choutman.allinn.koos.model.Match;
import nl.choutman.allinn.koos.vertx.messagecodecs.MatchMessageCodec;
import nl.choutman.allinn.koos.vertx.messagecodecs.TeamMessageCodec;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by choutman on 19/05/2017.
 */
public class ScheduleVerticle extends AbstractVerticle {
    private final Logger logger = LoggerFactory.getLogger(ScheduleVerticle.class);

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        super.start();

        setupEventBus();
    }

    private void setupEventBus() {
        EventBus eventBus = vertx.eventBus();

        eventBus.registerDefaultCodec(Match.class, new MatchMessageCodec(new TeamMessageCodec()));
        eventBus.consumer("competition.match", event -> {
            logger.debug(event.body());
        });
    }

    public static void main(String[] args) {
        Config hazelcastConfig = new Config();
        hazelcastConfig.getNetworkConfig().getJoin().getTcpIpConfig().addMember("127.0.0.1").setEnabled(true);
        hazelcastConfig.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);

        ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);
        Vertx.clusteredVertx(new VertxOptions().setClusterManager(mgr), result -> {
            if (result.succeeded()) {
                result.result().deployVerticle(new ScheduleVerticle());
            }
        });
    }
}
