package nl.choutman.allinn.koos;

import com.hazelcast.config.Config;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.FileSystem;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import nl.choutman.allinn.koos.dao.TeamDao;
import nl.choutman.allinn.koos.dao.TeamDaoImpl;
import nl.choutman.allinn.koos.model.Match;
import nl.choutman.allinn.koos.model.Position;
import nl.choutman.allinn.koos.model.Team;
import nl.choutman.allinn.koos.parsers.ScheduleParser;
import nl.choutman.allinn.koos.parsers.StandingsParser;
import nl.choutman.allinn.koos.parsers.TeamParser;
import nl.choutman.allinn.koos.vertx.messagecodecs.MatchMessageCodec;
import nl.choutman.allinn.koos.vertx.messagecodecs.TeamMessageCodec;

import java.util.List;
import java.util.Set;

import static io.vertx.ext.web.handler.BodyHandler.DEFAULT_UPLOADS_DIRECTORY;
import static java.io.File.separator;

/**
 * Created by choutman on 16/05/2017.
 */
public class CompetitionParsingVerticle extends AbstractVerticle {
    private Logger logger = LoggerFactory.getLogger(CompetitionParsingVerticle.class);

    private EventBus eventBus;
    private TeamDao teamDao = TeamDaoImpl.getInstance();

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        eventBus = vertx.eventBus();

        eventBus.registerDefaultCodec(Team.class, new TeamMessageCodec());
        eventBus.registerDefaultCodec(Match.class, new MatchMessageCodec(new TeamMessageCodec()));

        Router router = Router.router(vertx);
        router.route("/api/competition").handler(BodyHandler.create());
        router.post("/api/competition").handler(event -> {
            Set<FileUpload> fileUploads = event.fileUploads();
            FileUpload fileUpload = fileUploads.stream().findFirst().get();

            final FileSystem fileSystem = vertx.fileSystem();
            final String filePath = DEFAULT_UPLOADS_DIRECTORY + separator + fileUpload.fileName();

            fileSystem.exists(filePath, result -> {
                if (result.result()) fileSystem.deleteBlocking(filePath);

                fileSystem.move(fileUpload.uploadedFileName(), filePath, null);
            });

            event.response().setStatusCode(201).end();

            parseTeams(filePath);
            parseStandings(filePath);
            parseSchedule(filePath);
        });

        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(router::accept).listen(8081, result -> {
            if (result.succeeded()) startFuture.complete();
            else startFuture.fail(result.cause());
        });
    }

    private void parseTeams(String filePath) {
        try {
            final TeamParser teamParser = new TeamParser(filePath);
            teamParser.parseTeams(team -> {
                teamDao.addTeam(team);
                eventBus.publish("competition.team", team);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseStandings(String filePath) {
        try {
            final StandingsParser standingsParser = new StandingsParser(filePath);
            final List<Position> positions = standingsParser.parseStandings();

            logger.debug("publishing standings");

            eventBus.publish("competition.standings", Json.encode(positions));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseSchedule(String filePath) {
        try {
            final ScheduleParser scheduleParser = new ScheduleParser(filePath);
            scheduleParser.parseSchedule(match -> eventBus.publish("competition.match", match));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Config hazelcastConfig = new Config();
        hazelcastConfig.getNetworkConfig().getJoin().getTcpIpConfig().addMember("127.0.0.1").setEnabled(true);
        hazelcastConfig.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);

        ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);
        Vertx.clusteredVertx(new VertxOptions().setClusterManager(mgr), result -> {
            if (result.succeeded()) {
                result.result().deployVerticle(new CompetitionParsingVerticle());
            }
        });
    }

}
