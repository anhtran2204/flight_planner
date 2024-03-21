import java.util.LinkedList;

/**
 * A city node to add in to the graph
 */
public class City {
    /** Name of the city */
    private String name;

    /** Linked list of the paths reachable from the node - city */
    private LinkedList<Path> paths;

    public City(String name) {
        this.name = name;
        this.paths = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public LinkedList<Path> getPaths() {
        return paths;
    }

    /**
     * Adding a path to the sublist
     *
     * @param dest Destination city
     * @param cost Cost of the path
     * @param time Time traversing the path
     */
    public void addPath(String dest, int cost, int time) {
        if (!exists(dest)) {
            paths.add(new Path(dest, cost, time));
        }
    }

    /**
     * Get the path and its destination
     *
     * @param dest The destination city
     * @return The path object containing the dest
     */
    public Path getPath(String dest) {
        for (Path path : paths) {
            if (path.getDestination().equals(dest)) {
                return path;
            }
        }
        return null;
    }

    /**
     * Check if a destination city or the path exist.
     *
     * @param name Name of the destination city
     * @return True if city exists, False if not
     */
    public boolean exists(String name) {
        for (Path city : paths) {
            if (city.getDestination().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
