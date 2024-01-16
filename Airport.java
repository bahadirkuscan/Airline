public class Airport {
    public String airport_code;
    public Airfield airfield;
    public double latitude;
    public double longitude;
    public double parking_cost;
    public double cost = Double.POSITIVE_INFINITY;
    public int time = 0;
    public Airport parent;

    Airport(){}


    Airport(String airport_code, Airfield airfield, double latitude, double longitude, double parking_cost){
        this.airport_code = airport_code;
        this.airfield = airfield;
        this.latitude = latitude;
        this.longitude = longitude;
        this.parking_cost = parking_cost;
    }

    Airport(String airport_code, Airfield airfield, double latitude, double longitude, double parking_cost, double cost, int time, Airport parent){
        this.airport_code = airport_code;
        this.airfield = airfield;
        this.latitude = latitude;
        this.longitude = longitude;
        this.parking_cost = parking_cost;
        this.cost = cost;
        this.time = time;
        this.parent = parent;
    }

    public double weatherMultiplier(int time){
        int weather_code = airfield.weather_codes.get(time);
        return (1 + 0.2 * (weather_code & 1)) * (1 + 0.15 * (weather_code >>> 1 & 1)) * (1 + 0.1 * (weather_code >>> 2 & 1)) * (1 + 0.05 * (weather_code >>> 3 & 1)) * (1 + 0.05 * (weather_code >>> 4 & 1));
    }
}
