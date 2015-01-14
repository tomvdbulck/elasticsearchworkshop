package be.ordina.wes.exercises.model;

public class Marketing {
	
	private Integer cars;
	private Integer shoes;
	private Integer toys;
	private Integer fashion;
	private Integer music;
	private Integer garden;
	private Integer electronic;
	private Integer hifi;
	private Integer food;

	public Integer getCars() {
		return this.cars;
	}

	public void setCars(Integer cars) {
		this.cars = cars;
	}

	public Integer getShoes() {
		return this.shoes;
	}

	public void setShoes(Integer shoes) {
		this.shoes = shoes;
	}

	public Integer getToys() {
		return this.toys;
	}

	public void setToys(Integer toys) {
		this.toys = toys;
	}

	public Integer getFashion() {
		return this.fashion;
	}

	public void setFashion(Integer fashion) {
		this.fashion = fashion;
	}

	public Integer getMusic() {
		return this.music;
	}

	public void setMusic(Integer music) {
		this.music = music;
	}

	public Integer getGarden() {
		return this.garden;
	}

	public void setGarden(Integer garden) {
		this.garden = garden;
	}

	public Integer getElectronic() {
		return this.electronic;
	}

	public void setElectronic(Integer electronic) {
		this.electronic = electronic;
	}

	public Integer getHifi() {
		return this.hifi;
	}

	public void setHifi(Integer hifi) {
		this.hifi = hifi;
	}

	public Integer getFood() {
		return this.food;
	}

	public void setFood(Integer food) {
		this.food = food;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(super.getClass().getName() + "-");
		sb.append("  cars=" + this.cars);
		sb.append("  shoes=" + this.shoes);
		sb.append("  toys=" + this.toys);
		sb.append("  fashion=" + this.fashion);
		sb.append("  music=" + this.music);
		sb.append("  garden=" + this.garden);
		sb.append("  electronic=" + this.electronic);
		sb.append("  hifi=" + this.hifi);
		sb.append("  food=" + this.food);

		return sb.toString();
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (super.getClass() != obj.getClass())
			return false;
		Marketing other = (Marketing) obj;

		if (this.cars != other.cars)
			return false;
		if (this.shoes != other.shoes)
			return false;
		if (this.toys != other.toys)
			return false;
		if (this.fashion != other.fashion)
			return false;
		if (this.music != other.music)
			return false;
		if (this.garden != other.garden)
			return false;
		if (this.electronic != other.electronic)
			return false;
		if (this.hifi != other.hifi)
			return false;
		return (this.food == other.food);
	}
}
