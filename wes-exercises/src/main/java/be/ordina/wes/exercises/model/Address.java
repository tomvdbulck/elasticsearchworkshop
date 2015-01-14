package be.ordina.wes.exercises.model;

public class Address {
	private String country;
	private String zipcode;
	private String city;
	private String countrycode;

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipcode() {
		return this.zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountrycode() {
		return this.countrycode;
	}

	public void setCountrycode(String countrycode) {
		this.countrycode = countrycode;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("Address{");
		sb.append("country='").append(this.country).append('\'');
		sb.append(", zipcode='").append(this.zipcode).append('\'');
		sb.append(", city='").append(this.city).append('\'');
		sb.append(", countrycode='").append(this.countrycode).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
