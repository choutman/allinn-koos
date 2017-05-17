package nl.choutman.allinn.koos.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by choutman on 17/05/2017.
 */

@Getter
@ToString
public class Position {
    private final String teamName;
    private final int points;
    private final int gamesPlayed;

    @JsonCreator
    public Position(@JsonProperty("teamName") String teamName,@JsonProperty("points") int points, @JsonProperty("gamesPlayed") int gamesPlayed) {
        this.teamName = teamName;
        this.points = points;
        this.gamesPlayed = gamesPlayed;
    }

}