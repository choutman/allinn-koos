package nl.choutman.allinn.koos;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

/**
 * Created by choutman on 16/05/2017.
 */
public class CompetitionParsingVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        super.start();

        Router router = Router.router(vertx);
        router.post("/api/competition").handler(event -> event.response().setStatusCode(201).end());

        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(router::accept).listen(8081);
    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new CompetitionParsingVerticle());
    }
}
