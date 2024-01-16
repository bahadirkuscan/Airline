import java.util.HashMap;

public class MinHeapTimed {
    public int size = 0;
    public int capacity = 100;
    public Airport[] airports = new Airport[capacity];
    public HashMap<String,Integer> index_map = new HashMap<>();



    MinHeapTimed(){}

    MinHeapTimed(int size, int capacity, Airport[] airports, HashMap<String,Integer> index_map){
        this.size = size;
        this.capacity = capacity;
        this.airports = airports;
        this.index_map = index_map;
    }

    public void add(Airport airport){
        int hole_index = ++size;
        if (size + 1 == capacity){
            resize();
        }
        airports[hole_index] = airport;
        percolateUp(hole_index);

    }

    public Airport getMin(){
        if (size == 0){
            return null;
        }
        return airports[1];
    }


    public Airport deleteMin(){
        if (size == 0){
            return null;
        }
        Airport airport = airports[1];
        index_map.remove(airport.airport_code + airport.time);
        airports[1] = airports[size--];
        percolateDown(1);
        return airport;
    }

    public void buildHeap(){
        for( int i = size / 2; i > 0; i-- ){
            percolateDown(i);
        }
    }

    public void percolateUp(int hole_index){
        Airport airport = airports[hole_index];
        int parent = hole_index / 2;
        Airport parent_airport;
        while (parent > 0){
            parent_airport = airports[parent];
            if (airport.cost< parent_airport.cost){
                airports[hole_index] = parent_airport;
                index_map.put(parent_airport.airport_code + parent_airport.time, hole_index);
            }
            else{
                break;
            }
            hole_index /= 2;
            parent /= 2;
        }
        airports[hole_index] = airport;
        index_map.put(airport.airport_code + airport.time, hole_index);
    }

    private void percolateDown(int hole_index){
        int child = hole_index * 2;
        Airport child_airport;
        Airport temp = airports[hole_index];

        while (child <= size){
            child_airport = airports[child];
            if (child != size && child_airport.cost > airports[child+1].cost){
                child++;
                child_airport = airports[child];
            }
            if (temp.cost < child_airport.cost){
                break;
            }
            airports[hole_index] = child_airport;
            index_map.put(child_airport.airport_code + child_airport.time, hole_index);
            hole_index = child;
            child *= 2;
        }

        airports[hole_index] = temp;
        index_map.replace(temp.airport_code + temp.time, hole_index);
    }

    public void append(Airport airport){
        int index = ++size;
        if (size + 1 == capacity){
            resize();
        }
        airports[index] = airport;
        index_map.put(airport.airport_code + airport.time, index);
    }

    public void resize(){
        capacity *= 2;
        Airport[] new_array = new Airport[capacity];
        System.arraycopy(airports,1,new_array,1, size);
        airports = new_array;
    }

    public void decreaseKey(Airport airport){
        int index = index_map.get(airport.airport_code + airport.time);
        Airport former = airports[index];
        if (airport.cost < former.cost){
            airports[index] = airport;
            if (index > 1){
                percolateUp(index);
            }
        }
    }

    public boolean contains(Airport airport){
        return index_map.containsKey(airport.airport_code + airport.time);
    }


    public Airport getAirport(String key){
        Integer index = index_map.get(key);
        if (index == null){
            return null;
        }
        return airports[index];
    }
}