package nl.woodmanict.koos.model;

public class Team {
  private String name;
  private boolean playingDoubles;

  public Team(String name, boolean playingDoubles) {
    this.name = name;
    this.playingDoubles = playingDoubles;
  }

	public String getName() {
		return name;
	}

  public boolean isPlayingDoubles() {
    return playingDoubles;
  }

	@Override
	public String toString() {
		return "Team [name=" + name + ", playingDoubles=" + playingDoubles + "]";
	}

  @Override
  public boolean equals(Object o) {
    if (o instanceof Team) {
      Team otherTeam = (Team) o;
      return getName().equals(otherTeam.getName());
    }

    return false;
  }
}
