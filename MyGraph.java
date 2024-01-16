import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MyGraph {
    public HashMap<String, ArrayList<Airport>> adjacency_list = new HashMap<>();    // key = airport code
    public static int distance_limit1;
    public static int distance_limit2;



    MyGraph(){}

    public void addAirport(String airport_code){
        adjacency_list.put(airport_code, new ArrayList<>());
    }

    public void addDirection(String from_airport, Airport to_airport){
        adjacency_list.get(from_airport).add(to_airport);
    }

    public double getDistance(Airport start, Airport destination){
        return 12742 * Math.asin( Math.sqrt( Math.pow( Math.sin( Math.toRadians((destination.latitude - start.latitude)/2) ), 2) + Math.cos( Math.toRadians(start.latitude) ) * Math.cos( Math.toRadians(destination.latitude) ) * Math.pow( Math.sin( Math.toRadians((destination.longitude - start.longitude)/2) ), 2)));
    }

    public double getFlightCost(double distance, double start_weather, double destination_weather){
        return 300 * start_weather * destination_weather + distance;
    }

    public double findPath(Airport source, Airport destination, int time){
        MinHeap airport_heap = new MinHeap();
        source.cost = 0;
        source.parent = null;
        Main.airports.remove(source.airport_code);
        for (Airport a : Main.airports.values()){
            a.cost = Double.POSITIVE_INFINITY;
            airport_heap.append(a);
        }
        Main.airports.put(source.airport_code, source);

        for (Airport neighbour : adjacency_list.get(source.airport_code)){
            if (!airport_heap.contains(neighbour)){
                continue;
            }
            double new_cost = getFlightCost(getDistance(source, neighbour), source.weatherMultiplier(time), neighbour.weatherMultiplier(time));
            if (new_cost < neighbour.cost){
                airport_heap.decreaseKey(neighbour.airport_code, new_cost);
                neighbour.parent = source;
            }
        }

        while (airport_heap.size != 0){
            Airport new_source = airport_heap.deleteMin();
            if (new_source == destination){
                return new_source.cost;
            }
            for (Airport neighbour : adjacency_list.get(new_source.airport_code)){
                if (!airport_heap.contains(neighbour)){
                    continue;
                }
                double new_cost = new_source.cost + getFlightCost(getDistance(new_source, neighbour), new_source.weatherMultiplier(time), neighbour.weatherMultiplier(time));
                if (new_cost < neighbour.cost){
                    airport_heap.decreaseKey(neighbour.airport_code, new_cost);
                    neighbour.parent = new_source;
                }
            }
        }
        return 0;
    }

    public String findPath2(Airport source, Airport destination, int start_time, int deadline){
        HashSet<String> checked = new HashSet<>();
        checked.add(source.airport_code + start_time);
        MinHeapTimed airport_heap = new MinHeapTimed();
        Airport min_cost = new Airport();
        source.time = start_time;
        source.cost = 0;
        source.parent = null;
        // for park
        airport_heap.append(new Airport(source.airport_code, source.airfield, source.latitude, source.longitude, source.parking_cost, source.parking_cost, start_time + 21600, source));
        // neighbours
        for (Airport neighbour : adjacency_list.get(source.airport_code)){
            double distance = getDistance(source, neighbour);
            int arrival_time = start_time + timeIncrement(distance);
            double destination_cost = getFlightCost(distance, source.weatherMultiplier(start_time), neighbour.weatherMultiplier(arrival_time));
            airport_heap.append(new Airport(neighbour.airport_code, neighbour.airfield, neighbour.latitude, neighbour.longitude, neighbour.parking_cost, destination_cost, arrival_time, source));
        }
        airport_heap.buildHeap();

        while (airport_heap.size != 0){
            Airport new_source = airport_heap.deleteMin();
            checked.add(new_source.airport_code + new_source.time);
            if (new_source.airport_code.equals(destination.airport_code)){
                if (new_source.cost < min_cost.cost){
                    min_cost = new_source;
                }
                continue;
            }
            // for park
            if (new_source.time + 21600 <= deadline){
                Airport park = new Airport(new_source.airport_code, new_source.airfield, new_source.latitude, new_source.longitude, new_source.parking_cost, new_source.cost + new_source.parking_cost, new_source.time + 21600, new_source);
                if (!checked.contains(park.airport_code + park.time)){
                    if (airport_heap.contains(park)){
                        airport_heap.decreaseKey(park);
                    }
                    else {
                        airport_heap.add(park);
                    }
                }
            }
            // for neighbours
            for (Airport neighbour : adjacency_list.get(new_source.airport_code)){
                double distance = getDistance(new_source, neighbour);
                int arrival_time = new_source.time + timeIncrement(distance);
                if (arrival_time > deadline){
                    continue;
                }
                if (checked.contains(neighbour.airport_code + arrival_time)){
                    continue;
                }
                double destination_cost = new_source.cost + getFlightCost(distance, new_source.weatherMultiplier(new_source.time), neighbour.weatherMultiplier(arrival_time));
                Airport new_neighbour = new Airport(neighbour.airport_code, neighbour.airfield, neighbour.latitude, neighbour.longitude, neighbour.parking_cost, destination_cost, arrival_time, new_source);
                if (airport_heap.contains(new_neighbour)){
                    airport_heap.decreaseKey(new_neighbour);
                }
                else {
                    airport_heap.add(new_neighbour);
                }
            }
        }
        if (min_cost.cost == Double.POSITIVE_INFINITY){
            return "No possible solution.";
        }
        return pathString(min_cost) + String.format("%.5f", min_cost.cost);
    }


    public int timeIncrement(double distance){
        if (distance <= distance_limit1){
            return 21600;
        }
        else if (distance <= distance_limit2){
            return 43200;
        }
        return 64800;
    }

    public String pathString(Airport destination){
        String s = "";
        while (destination.parent != null){
            if (destination.airport_code.equals(destination.parent.airport_code)){
                s = "PARK " + s;
            }
            else {
                s = destination.airport_code + " " + s;
            }
            destination = destination.parent;
        }
        return destination.airport_code + " " + s;
    }
}
