package nl.woodmanict.koos.dao;

import nl.woodmanict.koos.model.Match;

import java.util.List;

public interface MatchDao {
  List<Match> getMatches();
  void addMatch(Match match);
}
