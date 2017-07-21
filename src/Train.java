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
	
	/*--------------------------------------
	 *	            RUN THREAD 
	 *--------------------------------------*/
	
	public void run()
	{
		while(true){
			currentStation.loadTrain(this);
			currentStation.departTrain(this);
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
	
	public boolean passengerWantsToDepart() {
		for (int x = 0; x < passengersOnTrain.size(); x++) {
			if (passengersOnTrain.get(x).isDeparting())
				return true;
		}
		
		return false;
	}
}
