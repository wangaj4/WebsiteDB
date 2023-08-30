import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MovieStarsCache {
    private static final MovieStarsCache instance = new MovieStarsCache();
    private Map<String, List<List<String>>> movieStarsMap = new HashMap<>();

    private MovieStarsCache() {

    }

    public static MovieStarsCache getInstance() {
        return instance;
    }

    public Map<String, List<List<String>>> getMovieStarsMap() {
        return movieStarsMap;
    }

    public void setMovieStarsMap(Map<String, List<List<String>>> map){
        movieStarsMap = map;
    }

}