import java.util.HashMap;

public class Airfield {
    public HashMap<Integer,Integer> weather_codes = new HashMap<>();    // key = time, value = weather code

    Airfield(int time, int weather_code){
        weather_codes.put(time, weather_code);
    }
}
