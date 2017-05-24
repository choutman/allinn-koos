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
import nl.choutman.allinn.koos.model.Position;
import nl.choutman.allinn.koos.vertx.cors.CORS;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by choutman on 10/05/2017.
 */
public class StandingsVerticle extends AbstractVerticle {
    private Logger logger = LoggerFactory.getLogger(StandingsVerticle.class);
    private List<Position> standings;

    public StandingsVerticle() {
        standings = new ArrayList<>();
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        super.start();

        setupRestService();
        setupEventBus();
    }

    private void setupEventBus() {
        EventBus eventBus = vertx.eventBus();
        eventBus.consumer("competition.standings", event -> {
            final JsonArray standingsArray = new JsonArray((String) event.body());
            standings = standingsArray.stream().map(o -> ((JsonObject) o).mapTo(Position.class)).collect(Collectors.toList());
        });
    }

    private void setupRestService() {
        Router router = Router.router(vertx);
        Route route = router.get("/api/standings");

        route.produces("application/json").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();

            final String origin = routingContext.request().getHeader("Origin");
            if (CORS.isAllowed(origin)) {
                response.putHeader("Access-Control-Allow-Origin", origin);
            }
            response.putHeader("Content-Type", "application/json; charset=utf-8");

            if (standings.isEmpty()) {
                response.setStatusCode(404).end();
            } else {
                final String responseBody = Json.encode(standings);
                response.setStatusCode(200).end(responseBody);
            }

        });

        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(router::accept).listen(8080);
    }

    public static void main(String[] args) {
        Config hazelcastConfig = new Config();
        hazelcastConfig.getNetworkConfig().getJoin().getTcpIpConfig().addMember("127.0.0.1").setEnabled(true);
        hazelcastConfig.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);

        ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);
        Vertx.clusteredVertx(new VertxOptions().setClusterManager(mgr), result -> {
            if (result.succeeded()) {
                result.result().deployVerticle(new StandingsVerticle());
            }
        });
    }
}
