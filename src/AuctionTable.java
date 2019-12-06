import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
import big.data.*;
/**
 * Maxwell Testa
 * 111713073
 * CSE214
 * R01
 * HW6
 *
 */
public class AuctionTable implements Serializable {
	private Hashtable<String, Auction> table;
	public AuctionTable() {
		table = new Hashtable<>();
	}
	/**
	 * @param URL
	 * @return
	 * @throws IllegalArgumentException
	 * Builds from the URl
	 */
	public static AuctionTable buildFromURL(String URL) throws IllegalArgumentException {
		try {
			AuctionTable newTable = new AuctionTable();	
			DataSource ds = DataSource.connect(URL).load();
			String[] seller_names = ds.fetchStringArray("listing/seller_info/seller_name");
			String[] bids = ds.fetchStringArray("listing/auction_info/current_bid");
			String[] times = ds.fetchStringArray("listing/auction_info/time_left");
			for (int i = 0; i < times.length; i++) {
				if(times[i].contains("hour") && times[i].contains("day")) {
					times[i] = times[i].replaceAll("hours", "");
					times[i] = times[i].replaceAll("hour", "");
					times[i] = times[i].replaceAll("days", "");
					times[i] = times[i].replaceAll("day", "");
					times[i] = times[i].replaceAll("\\+", "");
					times[i] = times[i].trim();
					String[] nums = times[i].split(" , ");
					int num = Integer.parseInt(nums[0])*24; 
					num += Integer.parseInt(nums[1]);
					times[i] = Integer.toString(num);
				}
				else if(times[i].contains("hour") && !times[i].contains("day")) {
					times[i].replaceAll("hours", "");
					times[i].replaceAll("hour", "");
					times[i] = Integer.toString((Integer.parseInt(times[i])));
				}
				else if(!times[i].contains("hour") && times[i].contains("day")) {
					times[i].replaceAll("day", "");
					times[i].replaceAll("days", "");
					times[i] = Integer.toString((Integer.parseInt(times[i]) * 24));
				}
			}
			String[] id = ds.fetchStringArray("listing/auction_info/id_num");
			String[] bidder_names = ds.fetchStringArray("listing/auction_info/high_bidder/bidder_name");
			String[] memory = ds.fetchStringArray("listing/item_info/memory");
			String[] drive = ds.fetchStringArray("listing/item_info/hard_drive");
			String[] cpu = ds.fetchStringArray("listing/item_info/cpu");
			String[] info = new String[memory.length];
			for (int i = 0; i < seller_names.length; i++) {
				info[i] = cpu[i] + " " + drive[i] + " " + memory[i];
				Auction auction = new Auction(Integer.parseInt(times[i]), id[i], seller_names[i], info[i], bidder_names[i], Double.parseDouble(bids[i].replaceAll("\\$", "").replaceAll(",", "")));
				newTable.putAuction(id[i], auction);
			}
			return newTable;
		}
		catch (Exception ex) {
			throw new IllegalArgumentException(URL + " could not be loaded.");
		}
	}
	/**
	 * @param auctionID
	 * @param auction
	 * @throws IllegalArgumentException
	 */
	public void putAuction(String auctionID, Auction auction) throws IllegalArgumentException {
		try {
			table.put(auctionID, auction);
		}
		catch (IllegalArgumentException ex){
			System.out.println("Couldn't add the auction");
		}
	}
	/**
	 * @param auctionID
	 * @return
	 */
	public Auction getAuction(String auctionID) {
		return table.get(auctionID);
	}
	/**
	 * @param numHours
	 * @throws IllegalArgumentException
	 */
	public void letTimePass(int numHours) throws IllegalArgumentException {
		try {
			Set<String> keys1 = table.keySet();
			for(String key: keys1) {
				table.get(key).decrementTime(numHours);
			}
		}
		catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException();
		}
	}
	/**
	 * removes expired auctions
	 * if time == 0
	 */
	public void removeExpiredAuctions() {
		Set<String> keys = table.keySet();
		ArrayList<String> keyArray= new ArrayList<String>();
		for(String key: keys) {
			if (table.get(key).getTimeRemaining() == 0) {
				keyArray.add(key);
			}
		}
		for (int i = 0; i < keyArray.size(); i++) {
			table.remove(keyArray.get(i));
		}
	}
	/**
	 * prints formatted table of the auctions
	 */
	public void printTable() {
		System.out.println("");
		System.out.println(String.format("%-12s%-15s%-20s%-25s%-15s%-15s", "Auction ID", "Current Bid", "Seller Name", "Buyer Name", "Time Remaining", "Item Info"));
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
		Set<String> keys = table.keySet();
		for(String key: keys) {
			System.out.println(table.get(key).toString());
		}
	}
}
