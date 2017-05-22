package nl.choutman.allinn.koos.vertx.messagecodecs;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import nl.choutman.allinn.koos.model.Team;

/**
 * Created by choutman on 20/05/2017.
 */
public class TeamMessageCodec implements MessageCodec<Team, Team> {
    @Override
    public void encodeToWire(Buffer buffer, Team team) {
        final String encodedTeam = Json.encode(team);
        buffer.appendString(encodedTeam);
    }

    @Override
    public Team decodeFromWire(int pos, Buffer buffer) {
        final JsonObject entry = buffer.toJsonObject();
        return decode(entry);
    }

    public Team decode(JsonObject encodedTeam) {
        return new Team(encodedTeam.getString("name"), encodedTeam.getBoolean("playingDoubles"));
    }

    @Override
    public Team transform(Team team) {
        return team;
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
