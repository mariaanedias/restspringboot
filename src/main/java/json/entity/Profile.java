package json.entity;

import java.util.List;

public class Profile {

	private String profile;
	private List<Fraction> fractions;
	private List<Readings> readings;
	
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public List<Fraction> getFractions() {
		return fractions;
	}
	public void setFractions(List<Fraction> fractions) {
		this.fractions = fractions;
	}
	public List<Readings> getReadings() {
		return readings;
	}
	public void setReadings(List<Readings> readings) {
		this.readings = readings;
	}	
}
