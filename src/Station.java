import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Station {
	private Semaphore loadingSpot;
	private final int stationNo;
	private Station nextStation;
	private Train currentlyLoading;
	private ArrayList<Passenger> passengersWaiting;
	public static int stationsSpawned = 0; // for stationNo purposes
	private boolean doorsOpened;
	
	public Station() {
		stationsSpawned++;
		stationNo = stationsSpawned;
		// isa lang yung free na "parking spot" sa station
		loadingSpot = new Semaphore(1);
		nextStation = null;
		System.out.println("Spawned Station " + stationNo + ".");
		passengersWaiting = new ArrayList<Passenger>();
		doorsOpened = false;
	}
	
	/*--------------------------------------
	 *	      SPAWNER / OTHER METHODS 
	 *--------------------------------------*/
	
	public void spawnPassenger(Station destination) {
		System.out.println("Called Station.spawnPassenger()");
		passengersWaiting.add(new Passenger(this, destination));
	}
	
	public void spawnTrain(int capacity) {
		System.out.println("Called Station.spawnTrain()");
		loadTrain(new Train(capacity, this));
	}
	
	public void loadTrain(Train t) {
		
		/* The function must not return until the train is 
		 * satisfactorily loaded (all passengers are in their
		 * seats, and either the train is full or all waiting
		 * passengers have boarded).
		 */
		try {
			loadingSpot.acquire();
			setCurrentlyLoading(t);
			doorsOpened = true;
			System.out.println("Loading Train " + t.getTrainNo() + 
				" in Station " + stationNo);
			while (passengersWaiting.isEmpty() == false && t.getSeats().availablePermits() > 0 && t.passengerWantsToDepart());
			System.out.println("Train " + currentlyLoading.getTrainNo() + 
					" in Station " + getStationNo() + " finished loading.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void departTrain(Train t) {
		System.out.println("Called Station.departStation()");
		loadingSpot.release();
		nextStation.loadTrain(t);
		setCurrentlyLoading(null);
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
	
	public boolean getDoorsOpened() {
		return doorsOpened;
	}
}
