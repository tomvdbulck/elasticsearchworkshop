package be.ordina.wes.core.model;

public class Beer {

	private int id;
	private String brand;
    private String category;
    private double alcohol;
    private double price;
	private String description;

	public Beer() {
		super();
	}
	
	public Beer(int id, String brand, String category,String description, double alcohol, double price) {
		this.id = id;
		this.brand = brand;
		this.category = category;
		this.alcohol = alcohol;
		this.price = price;
        this.description = description;
	}

	public int getId() {
		return id;
	}
	
	public String getBrand() {
		return brand;
	}

	public String getCategory() {
		return category;
	}

	public double getAlcohol() {
		return alcohol;
	}

	public double getPrice() {
		return price;
	}

	public String getDescription() {
		return description;
	}

}
