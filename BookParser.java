package BooksParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class BookParser {
	
	private static final String COMMA_DELIMITER = ",";
	private static final int TOP_AMOUNT = 5;
	private static Connection connect = null;
	private static Statement statement = null;
	private static ResultSet resultSet = null;
	private static PreparedStatement preparedStatement = null;
	
	public static void main(String[] args) {
		try {
			getFromDB();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void getFromCSVToDatabase() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("C:\\Users\\shara\\Downloads\\books.csv"));
			ArrayList<Book> bookList = new ArrayList<Book>();
			String line = "";
			br.readLine();
			while((line = br.readLine()) != null) {
				String[] bookDetails = line.split(COMMA_DELIMITER);
				if (bookDetails.length == 7) {
					Book book = new Book(bookDetails[0], bookDetails[1], Double.parseDouble(bookDetails[2]), 
							Integer.parseInt(bookDetails[3]), Double.parseDouble(bookDetails[4]), 
							Integer.parseInt(bookDetails[5]), bookDetails[6]);
					bookList.add(book);
				} else { 
					int lastQuoteLocation = line.lastIndexOf('\"');
					String[] newBookDetails = new String[5];
					bookDetails[0] = line.substring(1 , lastQuoteLocation);
					line = line.substring(lastQuoteLocation + 2);
					newBookDetails = line.split(COMMA_DELIMITER);
					for(int i = 1; i <= newBookDetails.length; i++)
						bookDetails[i] = newBookDetails[i-1];
						
					Book book = new Book(bookDetails[0], bookDetails[1], Double.parseDouble(bookDetails[2]), 
							Integer.parseInt(bookDetails[3]), Double.parseDouble(bookDetails[4]), 
							Integer.parseInt(bookDetails[5]), bookDetails[6]);
					bookList.add(book);
				}
			}

			showTopInList(bookList);
			
			for (Book book: bookList) {
				writeDB(book);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try { br.close(); }
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void writeDB(Book book) throws Exception {
		try {
			//loads the MySQL DRiver into our program.
			Class.forName("com.mysql.cj.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/amazonBooksDB", "root", "alexWa0720");
			statement = connect.createStatement();
			
			preparedStatement = connect.prepareStatement("insert into amazonBooksDB.books"
					+ " values(default, ?, ?, ?, ?, ?, ?, ?)");
			
			preparedStatement.setString(1, book.getName());
			preparedStatement.setString(2, book.getAuthor());
			preparedStatement.setDouble(3, book.getUserRating());
			preparedStatement.setInt(4, book.getReviews());
			preparedStatement.setDouble(5, book.getPrice());
			preparedStatement.setInt(6, book.getYear());
			preparedStatement.setString(7, book.getGenre());
			
			preparedStatement.executeUpdate();
		} catch(Exception e) {
			throw e;
		} finally {
			close();
		}
	}

	private static void close() {
		try {
			if(resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	private static void getFromDB() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/amazonBooksDB", "root", "alexWa0720");
		statement = connect.createStatement();
		
		preparedStatement = connect.prepareStatement("select * from amazonBooksDB.books");
	    resultSet = preparedStatement.executeQuery();
	    showTopInList(getBookList(resultSet));
	}
	
	private static ArrayList<Book> getBookList(ResultSet resultSet) throws SQLException {
		ArrayList<Book> bookList = new ArrayList<Book>();
		while(resultSet.next()) {
            String title = resultSet.getString("title");
            String author = resultSet.getString("author");
            double userRating = resultSet.getDouble("userrating");
            int reviews = resultSet.getInt("reviews");
            double price = resultSet.getDouble("price");
            int yearPublished = resultSet.getInt("yearcreation");
            String genre = resultSet.getString("genre");
            bookList.add(new Book(title, author, userRating, reviews, price, yearPublished, genre));
        }
        return bookList;
    }
	
	private static void showTopInList(ArrayList<Book> bookList) {
		Collections.sort(bookList);
		
		System.out.println("Top " + TOP_AMOUNT + " books of the past decade: ");
		for (int i = 0; i < TOP_AMOUNT; i++) {
			System.out.println((i+1) + ". " + bookList.get(i) + "\n");
		}
		
		Book[] fictionList = new Book[TOP_AMOUNT];
		Book[] nonFictionList = new Book[TOP_AMOUNT];
		int i = 0, k = 0;
		System.out.println("\nTop " + TOP_AMOUNT + " fiction books of the past decade:");
		while(fictionList[TOP_AMOUNT - 1] == null) { 
			if(bookList.get(i).getGenre().equals("Fiction")) {
				fictionList[k] = bookList.get(i);
				System.out.println((k+1) + ". " + fictionList[k] + "\n");
				k++;
			}
			i++;
		}
		i=0;
		k=0;
		System.out.println("\nTop " + TOP_AMOUNT + " non-fiction books of the past decade:");
		while(nonFictionList[TOP_AMOUNT - 1] == null) { 
			if(bookList.get(i).getGenre().equals("Non Fiction")) {
				nonFictionList[k] = bookList.get(i);
				System.out.println((k+1) + ". " + nonFictionList[k] + "\n");
				k++;
			}
			i++;
		}
		
		System.out.println("-- MVP Author --");
        System.out.println(bookList.get(0).getAuthor());

	}
	
}
