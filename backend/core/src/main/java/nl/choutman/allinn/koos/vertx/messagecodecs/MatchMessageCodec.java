package nl.choutman.allinn.koos.vertx.messagecodecs;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.BufferFactory;
import nl.choutman.allinn.koos.model.Match;
import nl.choutman.allinn.koos.model.Team;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Created by choutman on 20/05/2017.
 */
public class MatchMessageCodec implements MessageCodec<Match, Match> {
    private final TeamMessageCodec teamMessageCodec;

    public MatchMessageCodec(TeamMessageCodec teamMessageCodec) {
        this.teamMessageCodec = teamMessageCodec;
    }

    @Override
    public void encodeToWire(Buffer buffer, Match match) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.put("homeTeam", match.getHomeTeam());
        jsonObject.put("awayTeam", match.getAwayTeam());
        jsonObject.put("dateTime", match.getDateTime().format(DateTimeFormatter.ISO_DATE_TIME));

        buffer.appendString(jsonObject.encode());
    }

    @Override
    public Match decodeFromWire(int pos, Buffer buffer) {
        final JsonObject entry = buffer.getBuffer(pos, buffer.length()).toJsonObject();

        final JsonObject encodedHomeTeam = new JsonObject(entry.getString("homeTeam"));
        final JsonObject encodedAwayTeam = new JsonObject(entry.getString("awayTeam"));
        final String dateTimeString = entry.getString("dateTime");

        final Team homeTeam = teamMessageCodec.decode(encodedHomeTeam);
        final Team awayTeam = teamMessageCodec.decode(encodedAwayTeam);
        final LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME);

        return new Match(homeTeam, awayTeam, dateTime);
    }

    @Override
    public Match transform(Match match) {
        return match;
    }

    @Override
    public String name() {
        return getClass().getSimpleName();
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
