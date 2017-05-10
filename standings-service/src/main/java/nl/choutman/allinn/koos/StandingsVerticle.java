package nl.choutman.allinn.koos;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

/**
 * Created by choutman on 10/05/2017.
 */
public class StandingsVerticle extends AbstractVerticle {
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        super.start();

        Router router = Router.router(vertx);
        Route route = router.get("/api/standings");

        route.handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.end();
        });

        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(router::accept).listen(8080);
    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new StandingsVerticle());
    }
}
