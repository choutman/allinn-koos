package nl.choutman.allinn.koos;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.file.FileSystem;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.Set;

import static io.vertx.ext.web.handler.BodyHandler.DEFAULT_UPLOADS_DIRECTORY;
import static java.io.File.separator;

/**
 * Created by choutman on 16/05/2017.
 */
public class CompetitionParsingVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        super.start();

        Router router = Router.router(vertx);
        router.route("/api/competition").handler(BodyHandler.create());
        router.post("/api/competition").handler(event -> {
            Set<FileUpload> fileUploads = event.fileUploads();
            FileUpload fileUpload = fileUploads.stream().findFirst().get();

            final FileSystem fileSystem = vertx.fileSystem();
            final String to = DEFAULT_UPLOADS_DIRECTORY + separator + fileUpload.fileName();

            fileSystem.exists(to, result -> {
                if (result.result()) fileSystem.deleteBlocking(to);

                fileSystem.move(fileUpload.uploadedFileName(), to, null);
            });


            event.response().setStatusCode(201).end(fileUpload.name() + " " + fileUpload.uploadedFileName() + " " + fileUpload.fileName());
        });

        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(router::accept).listen(8081);
    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new CompetitionParsingVerticle());
    }
}
