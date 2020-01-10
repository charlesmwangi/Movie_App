package com.example.movieapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.movieapp.R;
import com.example.movieapp.adapter.MoviesAdapter;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.MovieResponse;
import com.example.movieapp.rest.MovieApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    private static Retrofit retrofit = null;
    private RecyclerView recyclerView = null;
    // insert your themoviedb.org API KEY here
    private final static String API_KEY = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        connectAndGetApiData();
    }

    // This method create an instance of Retrofit
    // set the base url
    public void connectAndGetApiData() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }


    MovieApiService movieApiService = retrofit.create(MovieApiService.class);
    Call<MovieResponse> call = movieApiService.getTopRatedMovies(API_KEY);

    call.enqueue(new Callback<MovieResponse>(){
        @Override
        public void onResponse (Call < MovieResponse > call, Response< MovieResponse > response){
        List<Movie> movies = response.body().getResults();
        recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.list_item_movie, getApplicationContext()));
        Log.d(TAG, "Number of movies received: " + movies.size());
    }
        @Override
        public void onFailure (Call < MovieResponse > call, Throwable throwable){
            Log.e(TAG, throwable.toString());
        }
    });
}
}