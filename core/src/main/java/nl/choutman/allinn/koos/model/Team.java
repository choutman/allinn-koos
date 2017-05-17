package nl.choutman.allinn.koos.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(of = "name")
public class Team {
    private String name;
    private boolean playingDoubles;

    @JsonCreator
    public Team(String name, boolean playingDoubles) {
        this.name = name;
        this.playingDoubles = playingDoubles;
    }
}
