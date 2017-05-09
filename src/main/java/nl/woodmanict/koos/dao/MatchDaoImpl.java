package nl.woodmanict.koos.dao;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import nl.woodmanict.koos.model.Match;

public class MatchDaoImpl implements MatchDao {

  private List<Match> matches;
  private static MatchDao instance;

  private MatchDaoImpl() {
    matches = new ArrayList<>();
  }

  public static MatchDao getInstance() {
    if (instance == null) {
      instance = new MatchDaoImpl();
    }

    return instance;
  }

  @Override
  public List<Match> getMatches() {
    return matches;
  }

  @Override
  public void addMatch(Match match) {
    matches.add(match);
  }
}
