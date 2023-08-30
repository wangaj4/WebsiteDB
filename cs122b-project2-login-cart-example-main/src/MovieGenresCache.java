import java.util.HashMap;

import java.util.List;
import java.util.Map;



public class MovieGenresCache {
    private static final MovieGenresCache instance = new MovieGenresCache();
    private Map<String, List<List<String>>> movieGenresMap = new HashMap<>();

    private MovieGenresCache() {

    }

    public static MovieGenresCache getInstance() {
        return instance;
    }

    public Map<String, List<List<String>>> getMovieGenresMap() {
        return movieGenresMap;
    }

    public void setMovieGenresMap(Map<String, List<List<String>>> map){
        movieGenresMap = map;
    }

}