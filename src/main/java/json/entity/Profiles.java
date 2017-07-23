package json.entity;

import java.util.List;

//Resource representation class
//Spring uses Jackson JSON library to automatically Marshall instances of type Profile to a JSON
public class Profiles {
	
	private List<Profile> profiles;

	public List<Profile> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<Profile> profiles) {
		this.profiles = profiles;
	}
}
