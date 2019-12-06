import java.io.Serializable;
/**
 * Maxwell Testa
 * 111713073
 * CSE214
 * R01
 * HW6
 *
 */
public class Auction implements Serializable{
	private int timeRemaining;
	private double currentBid;
	private String auctionID, sellerName, buyerName, itemInfo;
	public Auction() {
		
	}
	/**
	 * @param timeRemaining
	 * @param auctionID
	 * @param sellerName
	 * @param itemInfo
	 */
	public Auction(int timeRemaining, String auctionID, String sellerName, String itemInfo) {
		this.timeRemaining = timeRemaining;
		this.auctionID = auctionID;
		this.sellerName = sellerName;
		this.itemInfo = itemInfo;
		
	}
	/**
	 * @param timeRemaining
	 * @param auctionID
	 * @param sellerName
	 * @param itemInfo
	 * @param buyerName
	 * @param currentBid
	 */
	public Auction(int timeRemaining, String auctionID, String sellerName, String itemInfo, String buyerName, double currentBid) {
		this.timeRemaining = timeRemaining;
		this.auctionID = auctionID;
		this.sellerName = sellerName;
		this.itemInfo = itemInfo;
		this.buyerName = buyerName;
		this.currentBid = currentBid;
	}
	/**
	 * @param time
	 * decrements time
	 */
	public void decrementTime(int time) {
		if (this.timeRemaining - time <= 0) {
			this.timeRemaining = 0;
		}
		else {
			this.timeRemaining -= time;
		}
	}
	/**
	 * @param bidderName
	 * @param bidAmt
	 * @throws ClosedAuctionException
	 */
	public void newBid(String bidderName, double bidAmt) throws ClosedAuctionException{
		try {
			if (this.timeRemaining == 0) {
				throw new ClosedAuctionException();
			}
			else {
				if (bidAmt > this.currentBid) {
					this.buyerName = bidderName;
					this.currentBid = bidAmt;
				}
			}
		}
		catch(IllegalArgumentException ex) {
			System.out.println("Invalid input");
		}
	}
	/* 
	 * to string, formats table
	 */
	public String toString() {
		if (currentBid == 0.0) {
			return String.format("%-12s%-15s%-20s%-25s%-15s%-15s", auctionID, "", sellerName, "", timeRemaining, itemInfo);
		}
		else {
			return String.format("%-12s%-15s%-20s%-25s%-15s%-15s", auctionID, currentBid, sellerName, buyerName, timeRemaining, itemInfo);
		}
	}
	/**
	 * @returns time remaining
	 */
	public int getTimeRemaining() {
		return timeRemaining;
	}
	/**
	 * @returns current bid
	 */
	public double getCurrentBid() {
		return currentBid;
	}
	/**
	 * @returns auction ID
	 */
	public String getAuctionID() {
		return auctionID;
	}
	/**
	 * @returns seller name
	 */
	public String getSellerName() {
		return sellerName;
	}
	/**
	 * @returns buyerName
	 */
	public String getBuyerName() {
		return buyerName;
	}
	/**
	 * @returns itemInfo 
	 */
	public String getItemInfo() {
		return itemInfo;
	}
}
