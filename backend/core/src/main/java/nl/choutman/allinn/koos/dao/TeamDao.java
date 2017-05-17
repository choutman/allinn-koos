package nl.choutman.allinn.koos.dao;

import nl.choutman.allinn.koos.model.Team;

import java.util.List;
import java.util.Optional;

public interface TeamDao {
  List<Team> getTeams();
  void addTeam(Team team);
  Optional<Team> findTeam(String name);
}
