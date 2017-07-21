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
	
	public void enterStation(Station s) {
		// try papasok ng train sa loading part ng station
		currentStation = s;
		currentStation.receiveTrain(this);
		currentStation.setCurrentlyLoading(this);
	}
	
	public void departStation() {
		currentStation.getLoadingSpot().release();
		currentStation.getNextStation().receiveTrain(this);
	}
	
	/*--------------------------------------
	 *	            RUN THREAD 
	 *--------------------------------------*/
	
	public void run()
	{
		currentStation.receiveTrain(this);
		currentStation.loadTrain();
		departStation();
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
		try {
			currentStation.getPassengersWaiting().remove(p);
			passengersOnTrain.add(p);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void passengerDepartsFromTrain(Passenger p) {
		try {
			passengersOnTrain.remove(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
