package be.ordina.wes.core.model;

public class Address {
	
	private String street;
	private String city;
	private String zipCode;
	
	public Address() {
		super();
	}
	
	public Address(String street, String city, String zipCode) {
		this.street = street;
		this.city = city;
		this.zipCode = zipCode;
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getZipCode() {
		return zipCode;
	}

}