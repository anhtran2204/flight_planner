import java.util.ArrayList;

/**
 * Create an object storing a list of potential flight paths
 * given a source city and a destination city.
 */
public class Flights {
    /** List storing the potential flight paths */
    private ArrayList<Path> flights;

    public Flights(ArrayList<Path> flights) {
        this.flights = flights;
    }

    /**
     * Get total cost of each flight path.
     *
     * @return The total cost
     */
    public double getTotalCost() {
        double sum = 0;
        for (Path path : flights) {
            sum += path.getCost();
        }
        return sum;
    }

    /**
     * Get total time of each flight path
     *
     * @return The total time
     */
    public int getTotalTime() {
        int sum = 0;
        for (Path path : flights) {
            sum += path.getTime();
        }
        return sum;
    }

    public ArrayList<Path> getFlights() {
        return flights;
    }
}
