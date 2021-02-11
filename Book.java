package BooksParser;

public class Book implements Comparable<Book>{
	private String name;
	private String author;
	private double userRating;
	private int reviews;
	private double price;
	private int year;
	private String genre;
	
	public Book(String name, String author, double userRating, int reviews,
			double price, int year, String genre) {
		this.name = name;
		this.author = author;
		this.userRating = userRating;
		this.reviews = reviews;
		this.price = price;
		this.year = year;
		this.genre = genre;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public double getUserRating() {
		return this.userRating;
	}
	
	
	public int getReviews() {
		return this.reviews;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public int getYear() {
		return this.year;
	}
	
	public String getGenre() {
		return this.genre;
	}
	
	@Override
	public String toString() {
		return "Name: " + name + ", Author: " + author + 
				", UserRating: " + userRating + " Reviews: " + reviews +
				", Price: " + price + ", Year: " + year + ", Genre: " + genre;
	}
	
	public int compareTo(Book o) {
		if (userRating > o.userRating) 
			return -1;
		else if (userRating < o.userRating)
			return 1;
		else
			return 0;
	}
}
