package nl.woodmanict.koos.model;

import java.time.LocalDateTime;

public class Match {
  private Team homeTeam;
  private Team awayTeam;
  private LocalDateTime dateTime;

  public Match(Team home, Team away, LocalDateTime dateTime) {
    homeTeam = home;
    awayTeam = away;
    this.dateTime = dateTime;
  }

  public Team getHomeTeam() {
    return homeTeam;
  }

  public Team getAwayTeam() {
    return awayTeam;
  }

  public LocalDateTime getDateTime() {
    return dateTime;
  }

  @Override
  public String toString() {
    return "Home team: " + homeTeam + ";" +
            "Away team: " + awayTeam + ";" +
            "Date time: " + dateTime;
  }
}
