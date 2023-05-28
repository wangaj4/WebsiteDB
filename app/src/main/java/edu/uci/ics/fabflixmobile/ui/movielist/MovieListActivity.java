package edu.uci.ics.fabflixmobile.ui.movielist;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import edu.uci.ics.fabflixmobile.R;
import edu.uci.ics.fabflixmobile.data.NetworkManager;
import edu.uci.ics.fabflixmobile.data.model.Movie;
import edu.uci.ics.fabflixmobile.databinding.ActivityLoginBinding;
import edu.uci.ics.fabflixmobile.databinding.ActivityMovielistBinding;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;


public class MovieListActivity extends AppCompatActivity {

    private Button prevButton;
    private Button nextButton;
    final ArrayList<Movie> movies = new ArrayList<>();

    public interface MovieResponseCallback {
        void onResponse(ArrayList<Movie> movies);
        void onError(String errorMessage);
    }

    private final String host = "3.21.98.236";
    private final String port = "8443";
    private final String domain = "cs122b-project2-login-cart-example";
    private final String baseURL = "https://" + host + ":" + port + "/" + domain;


    public void getMovies(int page, final MovieResponseCallback callback){
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        final StringRequest movieRequest = new StringRequest(
                Request.Method.POST,
                baseURL + "/api/list",
                response -> {

                    try{
                        JSONObject jsonResponse = new JSONObject(response);
                        Iterator<String> titles = jsonResponse.keys();
                        while (titles.hasNext()) {
                            String title = titles.next();
                            Object array = jsonResponse.get(title);
                            if(array instanceof JSONArray){
                                Movie m = new Movie(title,((JSONArray) array).optString(0),((JSONArray) array).optString(1));
                                movies.add(m);
                            }

                        }
                        callback.onResponse(movies);
                    }catch(Exception e){
                        Log.d("MovieList.error", "JSON parsing error: " + e.getMessage());
                        callback.onError(e.getMessage());
                    }


                },
                error -> {
                    // error
                    Log.d("MovieList.error", error.toString());
                    callback.onError(error.toString());
                }){
                @Override
                protected Map<String, String> getParams() {
                    // POST request form data
                    final Map<String, String> params = new HashMap<>();
                    Intent intent = getIntent();

                    String searchText = intent.getStringExtra("searchText");
                    String Page = Integer.toString(page);
                    params.put("Full", searchText);
                    params.put("Page", Page);
                    return params;
                }
            };
        queue.add(movieRequest);
    }

    public void getMoviesMaster(int page){
        final Context context = this;
        getMovies(page, new MovieResponseCallback() {
            @Override
            public void onResponse(ArrayList<Movie> movies) {
                setContentView(R.layout.activity_movielist);
                MovieListViewAdapter adapter = new MovieListViewAdapter(context, movies);
                ListView listView = findViewById(R.id.list);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener((parent, view, position, id) -> {
                    Movie movie = movies.get(position);
                    @SuppressLint("DefaultLocale") String message = String.format("Clicked on position: %d, name: %s, %s", position, movie.getName(), movie.getYear());
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onError(String errorMessage) {
                Log.d("MovieListError.log",errorMessage);
            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getMoviesMaster(0);

        ActivityMovielistBinding binding = ActivityMovielistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prevButton = binding.button1;
        nextButton = binding.button2;

        prevButton.setOnClickListener(view -> prevPage());
        nextButton.setOnClickListener(view -> nextPage());


    }

    public void prevPage(){
        Intent intent = getIntent();
        int page = intent.getIntExtra("page", 0);
        if(page>0) page -=1;
        intent.putExtra("page",page);
        getMoviesMaster(page);
    }
    public void nextPage(){
        Intent intent = getIntent();
        int page = intent.getIntExtra("page", 0);
        page += 1;
        intent.putExtra("page",page);
        Log.d("MoviePage", Integer.toString(page));
        getMoviesMaster(page);
    }
}