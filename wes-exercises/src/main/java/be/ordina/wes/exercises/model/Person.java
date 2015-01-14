package be.ordina.wes.exercises.model;

import java.util.Date;

public class Person {
	
	private String name;
	private Date dateOfBirth;
	private String gender;
	private Integer children;
	private Marketing marketing;
	private Address address;

	public Person() {
		this.name = null;
		this.dateOfBirth = null;
		this.gender = null;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Marketing getMarketing() {
		return this.marketing;
	}

	public void setMarketing(Marketing marketing) {
		this.marketing = marketing;
	}

	public Address getAddress() {
		return this.address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Integer getChildren() {
		return this.children;
	}

	public void setChildren(Integer children) {
		this.children = children;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("Person{");
		sb.append("name='").append(this.name).append('\'');
		sb.append(", dateOfBirth=").append(this.dateOfBirth);
		sb.append(", gender='").append(this.gender).append('\'');
		sb.append(", children=").append(this.children);
		sb.append(", marketing=").append(this.marketing);
		sb.append(", address=").append(this.address);
		sb.append('}');
		return sb.toString();
	}
}
