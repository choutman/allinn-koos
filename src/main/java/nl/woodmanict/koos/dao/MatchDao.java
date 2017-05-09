package nl.woodmanict.koos.dao;

import nl.woodmanict.koos.model.Match;
import java.util.List;
import java.util.Optional;

public interface MatchDao {
  List<Match> getMatches();
  void addMatch(Match match);
}
