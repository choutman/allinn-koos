package nl.choutman.allinn.koos;

import com.hazelcast.config.Config;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import nl.choutman.allinn.koos.model.Match;
import nl.choutman.allinn.koos.vertx.cors.CORS;
import nl.choutman.allinn.koos.vertx.messagecodecs.MatchMessageCodec;
import nl.choutman.allinn.koos.vertx.messagecodecs.TeamMessageCodec;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by choutman on 19/05/2017.
 */
public class ScheduleVerticle extends AbstractVerticle {
    private final Logger logger = LoggerFactory.getLogger(ScheduleVerticle.class);

    private final List<Match> schedule;

    public ScheduleVerticle() {
        this.schedule = new ArrayList<>();
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        super.start();

        setupEventBus();
        setupRestService();
    }

    private void setupEventBus() {
        EventBus eventBus = vertx.eventBus();

        eventBus.registerDefaultCodec(Match.class, new MatchMessageCodec(new TeamMessageCodec()));
        eventBus.consumer("competition.match", event -> schedule.add((Match) event.body()));
    }

    private void setupRestService() {
        Router router = Router.router(vertx);
        Route route = router.get("/api/schedule");

        route.produces("application/json").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();

            final String origin = routingContext.request().getHeader("Origin");
            if (CORS.isAllowed(origin)) {
                response.putHeader("Access-Control-Allow-Origin", origin);
            }
            response.putHeader("Content-Type", "application/json; charset=utf-8");

            if (schedule.isEmpty()) {
                response.setStatusCode(404).end();
            }
            else {
                final JsonArray scheduleArray = new JsonArray();
                schedule.forEach(match -> {
                    final JsonObject jsonObject = new JsonObject();
                    jsonObject.put("homeTeamName", match.getHomeTeam().getName());
                    jsonObject.put("awayTeamName", match.getAwayTeam().getName());
                    jsonObject.put("dateTime", match.getDateTime().format(DateTimeFormatter.ISO_DATE_TIME));

                    scheduleArray.add(jsonObject);
                });

                response.setStatusCode(200).end(scheduleArray.encode());
            }
        });

        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(router::accept).listen(8082);
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
