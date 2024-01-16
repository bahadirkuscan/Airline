import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static HashMap<String,Airport> airports = new HashMap<>();

    public static void main(String[] args) throws FileNotFoundException {
        HashMap<String,Airfield> airfields = new HashMap<>();
        MyGraph airport_graph = new MyGraph();

        Scanner weather_reader = new Scanner(new File("C:\\Users\\ASUS\\IdeaProjects\\Project4\\cases_v3\\cases\\weather.csv"));
        Scanner airport_reader = new Scanner(new File("C:\\Users\\ASUS\\IdeaProjects\\Project4\\cases_v3\\cases\\airports\\TR-0.csv"));
        Scanner direction_reader = new Scanner(new File("C:\\Users\\ASUS\\IdeaProjects\\Project4\\cases_v3\\cases\\directions\\TR-0.csv"));
        Scanner mission_reader = new Scanner(new File("C:\\Users\\ASUS\\IdeaProjects\\Project4\\cases_v3\\cases\\missions\\TR-0.in"));
        PrintWriter task1 = new PrintWriter("C:\\Users\\ASUS\\IdeaProjects\\Project4\\src\\aaa.txt");
        PrintWriter task2 = new PrintWriter("C:\\Users\\ASUS\\IdeaProjects\\Project4\\src\\bbb.txt");

        weather_reader.nextLine();
        while (weather_reader.hasNext()){
            String[] line = weather_reader.nextLine().split(",");
            if (airfields.containsKey(line[0])){
                airfields.get(line[0]).weather_codes.put(Integer.parseInt(line[1]), Integer.parseInt(line[2]));
            }
            else {
                airfields.put(line[0], new Airfield(Integer.parseInt(line[1]), Integer.parseInt(line[2])));
            }
        }
        weather_reader.close();

        airport_reader.nextLine();
        while (airport_reader.hasNext()){
            String[] line = airport_reader.nextLine().split(",");
            String airport_code = line[0];
            airports.put(airport_code, new Airport(airport_code, airfields.get(line[1]), Double.parseDouble(line[2]), Double.parseDouble(line[3]), Double.parseDouble(line[4])));
            airport_graph.addAirport(airport_code);
        }
        airport_reader.close();

        direction_reader.nextLine();
        while (direction_reader.hasNext()){
            String[] line = direction_reader.nextLine().split(",");
            airport_graph.addDirection(line[0], airports.get(line[1]));
        }
        direction_reader.close();

        String plane = mission_reader.nextLine();
        switch (plane) {
            case "Orion III" -> {
                MyGraph.distance_limit1 = 1500;
                MyGraph.distance_limit2 = 3000;
            }
            case "Skyfleet S570" -> {
                MyGraph.distance_limit1 = 500;
                MyGraph.distance_limit2 = 1000;
            }
            case "T-16 Skyhopper" -> {
                MyGraph.distance_limit1 = 2500;
                MyGraph.distance_limit2 = 5000;
            }
            case "Carreidas 160" -> {
                MyGraph.distance_limit1 = 175;
                MyGraph.distance_limit2 = 350;
            }
        }
        while (mission_reader.hasNext()){
            String[] line = mission_reader.nextLine().split(" ");
            double cost = airport_graph.findPath(airports.get(line[0]), airports.get(line[1]), Integer.parseInt(line[2]));
            task1.write(airport_graph.pathString(airports.get(line[1])) + String.format("%.5f",cost));
            task1.write("\n");
            task2.write(airport_graph.findPath2(airports.get(line[0]), airports.get(line[1]), Integer.parseInt(line[2]), Integer.parseInt(line[3])));
            task2.write("\n");
        }
        mission_reader.close();
        task1.close();
        task2.close();
    }
}
