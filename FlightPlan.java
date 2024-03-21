import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class FlightPlan {
    /** BufferedReader objects to read in both input files */
    private static BufferedReader flightData, requestedFlightData;
    /** PrintWriter objects to write into output file */
    private static PrintWriter flightPlans;
    /** Nested linked lists to store adjacency list of all flight paths */
    private static LinkedList<City> availableFlightPlans = new LinkedList<>();
    /** ArrayList containing Flight objects storing potential paths from src to dest */
    private static ArrayList<Flights> desiredFlights = new ArrayList<>();
    /** Arrays containing the split data from the input files */
    private static String[][] pathCostTime, requestedPathList;

    public static void main(String[] args) {
        getAvailableFlights(args);
        getDesiredFlights(args);
    }

    /**
     * Read both input files from command line retrieve data and split into an array.
     * Additionally add city vertices into the global adjacency list.
     *
     * @param args Input file name from command line
     */
    public static void getAvailableFlights(String[] args) {
        try {
            flightData = new BufferedReader(new FileReader(args[0]));
            requestedFlightData = new BufferedReader(new FileReader(args[1]));
            pathCostTime = new String[Integer.parseInt(flightData.readLine())][4];
            requestedPathList = new String[Integer.parseInt(requestedFlightData.readLine())][3];
            String input;
            int i = 0;
            while ((input = flightData.readLine()) != null) {
                String[] splittedInput = input.split("\\|");
                pathCostTime[i] = splittedInput;
                String city1 = pathCostTime[i][0];
                String city2 = pathCostTime[i][1];
                int cost = Integer.parseInt(pathCostTime[i][2]);
                int time = Integer.parseInt(pathCostTime[i][3]);
                addCity(city1, city2, cost, time);
                addCity(city2, city1, cost, time);
                i++;
            }

            i = 0;
            while ((input = requestedFlightData.readLine()) != null) {
                String[] splittedInput = input.split("\\|");
                requestedPathList[i] = splittedInput;
                i++;
            }
            flightData.close();
            requestedFlightData.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get requested flights data and sort type and
     * call getAllPaths() of src and dest city then
     * proceed to write into output file.
     *
     * @param args Output file name from command line
     */
    public static void getDesiredFlights(String[] args) {
        try {
            flightPlans = new PrintWriter(args[2]);
            for (int i = 0; i < requestedPathList.length; i++) {
                if (requestedPathList[i][2].equals("C")) {
                    flightPlans.printf("Flight %d: %s, %s (Cost)\n", i + 1, requestedPathList[i][0], requestedPathList[i][1]);
                } else if (requestedPathList[i][2].equals("T")) {
                    flightPlans.printf("Flight %d: %s, %s (Time)\n", i + 1, requestedPathList[i][0], requestedPathList[i][1]);
                }
                getAllPaths(requestedPathList[i][0], requestedPathList[i][1], requestedPathList[i][2]);
            }
            flightPlans.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a city into the graph as vertices
     * with sublist of reachable neighbor cities.
     *
     * @param src City that we start at
     * @param dest City we want to reach
     * @param cost Cost of the traversal
     * @param time Time takes for the traversal
     */
    public static void addCity(String src, String dest, int cost, int time) {
        City newCity = new City(src);
        if (availableFlightPlans.isEmpty()) {
            availableFlightPlans.add(newCity);
            newCity.addPath(dest, cost, time);
        }

        if (!exists(src)) {
            availableFlightPlans.add(newCity);
            newCity.addPath(dest, cost, time);
        } else {
            City city = findCity(src);
            assert city != null;
            city.addPath(dest, cost, time);
        }
    }

    /**
     * Check if a city exists in the graph
     *
     * @param name Name of the city we want to find
     * @return True if city exist, False if not
     */
    public static boolean exists(String name) {
        for (City city : availableFlightPlans) {
            if (city.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Find a city within our graph
     *
     * @param name Name of the city needed to find
     * @return The city object
     */
    public static City findCity(String name) {
        for (City city : availableFlightPlans) {
            if (city.getName().equals(name)) {
                return city;
            }
        }
        return null;
    }

    /**
     * Traverse the graph and find all possible paths from
     * start to dest and print out in order of cost or time
     *
     * @param start Starting city
     * @param dest Destination city
     * @param sortType Sorting category
     */
    public static void getAllPaths(String start, String dest, String sortType) {
        desiredFlights = new ArrayList<>();
        ArrayList<String> isVisited = new ArrayList<>();
        ArrayList<Path> temp = new ArrayList<>();
        DFS(start, dest, isVisited, temp);

        if (sortType.equals("C")) {
            desiredFlights.sort(Comparator.comparingDouble(Flights::getTotalCost));
        } else if (sortType.equals("T")) {
            desiredFlights.sort(Comparator.comparingInt(Flights::getTotalTime));
        }

        int pathNum = 1;
        for (Flights flights : desiredFlights) {
            flightPlans.print("Path " + pathNum + ": " + start + " ---> ");
            for (int i = 0; i < flights.getFlights().size(); i++) {
                Path path = flights.getFlights().get(i);
                if (i == flights.getFlights().size() - 1) {
                    flightPlans.print(path.getDestination() + ". ");
                } else {
                    flightPlans.print(path.getDestination() + " ---> ");
                }
            }
            pathNum++;
            flightPlans.print("Time: " + flights.getTotalTime());
            flightPlans.printf(" Cost: %.2f\n", flights.getTotalCost());
        }
        flightPlans.println();
    }

    /**
     * Depth-first Graph traversal algorithm that exhaustively search for
     * all simple paths from source to destination using the adjacency list
     * by going down each path of an unmarked node and backtracks to go down
     * other unexplored paths before moving onto adjacent nodes.
     *
     * @param start Starting city
     * @param dest Destination city
     * @param isVisited List for marking visited cities
     * @param pathList List storing the path traverse
     */
    public static void DFS(String start, String dest, ArrayList<String> isVisited, ArrayList<Path> pathList) {
        if (start.equals(dest)) {
            desiredFlights.add(new Flights(new ArrayList<>(pathList)));
            return;
        }

        isVisited.add(start);

        int index = availableFlightPlans.indexOf(findCity(start));
        LinkedList<Path> currPath = availableFlightPlans.get(index).getPaths();
        for (Path path : currPath) {
            if (!isVisited.contains(path.getDestination())) {
                pathList.add(path);
                DFS(path.getDestination(), dest, isVisited, pathList);
                pathList.remove(path);
            }
        }
        isVisited.remove(start);
    }
}
