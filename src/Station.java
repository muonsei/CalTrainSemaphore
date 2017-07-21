import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Station {
	private Semaphore loadingSpot;
	private final int stationNo;
	private Station nextStation;
	private Train currentlyLoading;
	private ArrayList<Passenger> passengersWaiting;
	public static int stationsSpawned = 0; // for stationNo purposes
	
	public Station() {
		stationsSpawned++;
		stationNo = stationsSpawned;
		// isa lang yung free na "parking spot" sa station
		loadingSpot = new Semaphore(1);
		nextStation = null;
		System.out.println("Spawned Station " + stationNo + ".");
		passengersWaiting = new ArrayList<Passenger>();
	}
	
	/*--------------------------------------
	 *	      SPAWNER / OTHER METHODS 
	 *--------------------------------------*/
	
	public void spawnPassenger(Station destination) {
		passengersWaiting.add(new Passenger(this, destination));
	}
	
	public void spawnTrain(int capacity) {
		receiveTrain(new Train(capacity, this));
	}
	
	public void receiveTrain(Train t) {
		try {
			loadingSpot.acquire();
			setCurrentlyLoading(t);
			System.out.println("Received Train " + t.getTrainNo() + 
				" in Station " + stationNo);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadTrain() {
		
		/* The function must not return until the train is 
		 * satisfactorily loaded (all passengers are in their
		 * seats, and either the train is full or all waiting
		 * passengers have boarded).
		 */
		System.out.println("Entered loadTrain() method");
		while ((currentlyLoading.getSeats().availablePermits() > 0) || (!passengersWaiting.isEmpty()));
		System.out.println("Train " + currentlyLoading.getTrainNo() + 
			" in Station " + getStationNo() + " finished loading.");
	}
	
	/*--------------------------------------
	 *	        GETTERS / SETTERS 
	 *--------------------------------------*/
	
	public Semaphore getLoadingSpot() {
		return loadingSpot;
	}
	
	public int getStationNo() {
		return stationNo;
	}
	
	public Station getNextStation() {
		return nextStation;
	}
	
	public void setNextStation(Station s) {
		nextStation = s;
	}
	
	public Train getCurrentlyLoading() {
		return currentlyLoading;
	}
	
	public void setCurrentlyLoading(Train t) {
		currentlyLoading = t;
	}
	
	public ArrayList<Passenger> getPassengersWaiting() {
		return passengersWaiting;
	}
}
