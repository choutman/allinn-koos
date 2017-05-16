package nl.choutman.allinn.koos.dao;

import nl.choutman.allinn.koos.model.Match;

import java.util.List;

public interface MatchDao {
  List<Match> getMatches();
  void addMatch(Match match);
}
