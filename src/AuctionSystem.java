import java.io.FileInputStream;
import java.util.Hashtable;
import java.util.InputMismatchException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Scanner;
/**
/**
 * Maxwell Testa
 * 111713073
 * CSE214
 * R01
 * HW6
 *
 */
public class AuctionSystem implements Serializable{
	private AuctionTable auctionTable;
	private String username;
	/**
	 * @param username
	 * constructor for Auction
	 */
	public AuctionSystem(String username) {
		this.username = username;
	}
	/**
	 * @param args
	 * main method
	 */
	public static void main(String[] args) {
		boolean on = true;
		Scanner sc = new Scanner(System.in);
		System.out.println("Starting");
		System.out.println("Please select a username: ");
		String username = sc.nextLine();
		AuctionSystem auction = new AuctionSystem(username);
		try {
			FileInputStream file = new FileInputStream("auction.obj");
			ObjectInputStream inStream = new ObjectInputStream(file);
			auction.auctionTable = (AuctionTable)inStream.readObject();
			inStream.close();
			System.out.println("Auctions loaded");
		}
		catch(IOException | ClassNotFoundException ex) {
			auction.auctionTable = new AuctionTable();
			System.out.println("No auction found, creating new table");
		}
		while (on = true) {
			System.out.println("\nMenu: ");
			System.out.println("\t(D) - Import Data from URL");
			System.out.println("\t(A) - Create a New Auction");
			System.out.println("\t(B) - Bid on an Item");
			System.out.println("\t(I) - Get Info on Auction");
			System.out.println("\t(P) - Print All Auctions");
			System.out.println("\t(R) - Remove Expired Auctions");
			System.out.println("\t(T) - Let Time Pass");
			System.out.println("\t(Q) - Quit\n");
			System.out.print("Enter a selection: ");
			String selection = sc.nextLine().toUpperCase();
			if (selection.equals("D")) {
				System.out.println("Please enter a URL: ");
				String input = sc.nextLine();
				try {
					auction.auctionTable = AuctionTable.buildFromURL(input);
					auction.auctionTable.printTable();
				}
				catch (IllegalArgumentException ex){
					System.out.println("URL couldn't be read");
				}
			}
			else if(selection.equals("A")) {
				System.out.println("Creating new Auction as " + auction.username);
				System.out.println("Please enter an Auction ID: ");
				String id = sc.nextLine();
				System.out.println("Please enter an Auction time (hours): ");
				int hours = sc.nextInt();
				sc.nextLine();
				System.out.println("Please enter some Item Info: ");
				String info = sc.nextLine();
				Auction newAuction = new Auction(hours, id, auction.username, info);
				auction.auctionTable.putAuction(id, newAuction);
			}
			else if(selection.equals("B")) {
				try {
					System.out.println("Please enter an Auction ID: ");
					String id = sc.nextLine();
					auction.auctionTable.getAuction(id);
					System.out.println("Please enter a bid amount: ");
					auction.auctionTable.getAuction(id).newBid(username, sc.nextDouble());
					sc.nextLine();
					System.out.println("Bid attempted");
					sc.nextLine();
				}
				catch(InputMismatchException ex) {
					System.out.println("Illegal argument");
				}
				catch (ClosedAuctionException e) {
					System.out.println("Auction closed");
				}
			}
			else if(selection.equals("I")) {
				System.out.println("Enter an auction ID: ");
				String id = sc.nextLine();
				System.out.println(String.format("%-12s%-15s%-20s%-25s%-15s%-15s", "Auction ID", "Current Bid", "Seller Name", "Buyer Name", "Time Remaining", "Item Info"));
				System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
				System.out.println(auction.auctionTable.getAuction(id).toString());
			}
			else if(selection.equals("P")) {
				auction.auctionTable.printTable();
			}
			else if(selection.equals("R")) {
				auction.auctionTable.removeExpiredAuctions();
				System.out.println("Removing expired auctions...\n" + 
						"All expired auctions removed.");
			}
			else if(selection.equals("T")) {
				try {
					System.out.println("How many hours should pass: ");
					int hours = sc.nextInt();
					sc.nextLine();
					auction.auctionTable.letTimePass(hours);
					System.out.println("Time passing");
					System.out.println("Auction times updated.");
				}
				catch(IllegalArgumentException ex) {
					System.out.println("Illegal hour input");
				}
			}
			else if(selection.equals("Q")) {
				try {
					FileOutputStream file = new FileOutputStream("auction.obj");
					ObjectOutputStream outStream = new ObjectOutputStream(file);
					outStream.writeObject(auction.auctionTable);
					outStream.close();
					System.out.println("Saved to disk");
				}
				catch(IOException ex) {
					System.out.println("Error occured");
				}
				System.out.println("Done!");
				System.out.println("\nGoodbye.");
				on = false;
				break;
			}
			else {
				System.out.println("Invalid Input");
			}
		}
	}
}
