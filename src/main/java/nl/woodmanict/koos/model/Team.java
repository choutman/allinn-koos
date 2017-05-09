package nl.woodmanict.koos.model;

public class Team {
  private String name;
  private String contactPerson;

  public Team(String name) {
    this.name = name;
  }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	@Override
	public String toString() {
		return "Team [name=" + name + ", contactPerson=" + contactPerson + "]";
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
