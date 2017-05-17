package nl.choutman.allinn.koos.dao;

import nl.choutman.allinn.koos.model.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeamDaoImpl implements TeamDao {

  private List<Team> teams;
  private static TeamDao instance;

  private TeamDaoImpl() {
    teams = new ArrayList<>();
  }

  public static TeamDao getInstance() {
    if (instance == null) {
      instance = new TeamDaoImpl();
    }

    return instance;
  }

  @Override
  public List<Team> getTeams() {
    return teams;
  }

  @Override
  public void addTeam(Team team) {
    teams.add(team);
  }

  @Override
  public Optional<Team> findTeam(String name) {
    return teams.stream().filter(team -> team.getName().equals(name)).findFirst();
  }
}
