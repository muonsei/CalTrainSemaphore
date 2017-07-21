import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Train extends Thread {
	private Semaphore seats;
	private final int trainNo;
	private Station currentStation;
	private ArrayList<Passenger> passengersOnTrain;
	public static int trainsSpawned = 0; // for trainNo purposes
	
	public Train (int cap, Station sourceStation) {
		trainsSpawned++;
		trainNo = trainsSpawned;
		seats = new Semaphore(cap);
		currentStation = sourceStation;
		passengersOnTrain = new ArrayList<Passenger>();
		this.start();
		System.out.println("Spawned Train " + trainNo + 
			" in Station " + sourceStation.getStationNo());
	}
	
	public void departStation() {
		System.out.println("Called Train.departStation()");
		currentStation.getLoadingSpot().release();
		currentStation = currentStation.getNextStation();
	}
	
	/*--------------------------------------
	 *	            RUN THREAD 
	 *--------------------------------------*/
	
	public void run()
	{
		while(true){
			if (currentStation.loadTrain(this)) {
				System.out.println("Station.loadTrain() returned true.");
				departStation();
			}
		}
	}
	
	/*--------------------------------------
	 *	        GETTERS / SETTERS 
	 *--------------------------------------*/
	
	public Semaphore getSeats() {
		return seats;
	}
	
	public int getTrainNo() {
		return trainNo;
	}
	
	public Station getCurrentStation() {
		return currentStation;
	}
	
	public void setCurrentStation(Station s) {
		currentStation = s;
	}
	
	public void passengerRidesTrain(Passenger p){
		System.out.println("Called Train.passengerRidesTrain()");
		
		try {
			currentStation.getPassengersWaiting().remove(p);
			passengersOnTrain.add(p);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void passengerDepartsFromTrain(Passenger p) {
		System.out.println("Called Train.passengerDepartsFromTrain()");
		
		try {
			passengersOnTrain.remove(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
