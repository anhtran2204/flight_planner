/**
 * An path - "edge" - in the graph connecting two cities
 */
public class Path {
    /** Name of destination city */
    private String destination;

    /** Cost of the path */
    private int cost;

    /** Time of traversal */
    private int time;

    public Path(String destination, int cost, int time) {
        this.destination = destination;
        this.cost = cost;
        this.time = time;
    }

    public String getDestination() {
        return destination;
    }

    public int getCost() {
        return cost;
    }

    public int getTime() {
        return time;
    }
}
