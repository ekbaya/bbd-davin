package com.example.hbddavin.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.hbddavin.R;
import com.example.hbddavin.adapters.AdapterPosts;
import com.example.hbddavin.apis.PostAPI;
import com.example.hbddavin.modals.ModelPost;
import com.example.hbddavin.services.PostListener;
import com.example.hbddavin.services.SoundService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener ,
        PostListener , SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout swipeToRefresh;
    RecyclerView recyclerView;
    AdapterPosts adapterPosts;
    PostAPI postAPI;
    String mUID;
    //Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Family and Friends Posts");
        //init
        mAuth = FirebaseAuth.getInstance();
        checkUser();
        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setOnRefreshListener(this);
        //recycler view and its properties
        recyclerView = findViewById(R.id.posts_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // show newest posts first, for this load from last
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        layoutManager.setReverseLayout(true);
        // set layout to recyclerView
        recyclerView.setLayoutManager(layoutManager);

        postAPI = new PostAPI();
        postAPI.setPostListener(this);
        loadPosts();

    }

    private void checkUser() {
        // get current user
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
           // user is logged in stay here
        } else {
            // user is not signed in...
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //searchView to search product by product name/description
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // get item id
        int id = item.getItemId();
        if (id == R.id.action_add_post){
            startActivity(new Intent(this, AddPostActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadPosts() {
        postAPI.loadPosts();
    }

    private void searchPost(String s) {
        postAPI.searchPosts(s);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //called when user press search button
        if (!TextUtils.isEmpty(query)){
            searchPost(query);
        }
        else {
            loadPosts();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //called as and when user press any letter
        if (!TextUtils.isEmpty(newText)){
            searchPost(newText);
        }
        else {
            loadPosts();
        }
        return false;
    }

    @Override
    public void onImageUploadFailure(Exception e) {

    }

    @Override
    public void onPostUploaded() {

    }

    @Override
    public void onPostUploadFailure(Exception e) {

    }

    @Override
    public void onPostDataReceived(List<ModelPost> postList) {
        Log.d("POSTS DATA", postList.toString());
        //adapter
        adapterPosts = new AdapterPosts(this, postList);
        //set adapter to recyclerview
        recyclerView.setAdapter(adapterPosts);
    }

    @Override
    public void onDatabaseCancelled(DatabaseError error) {
        Toast.makeText(this, "Error loading posts: "+error.toString(), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onRefresh() {
       postAPI.loadPosts();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(swipeToRefresh.isRefreshing()) {
                    swipeToRefresh.setRefreshing(false);
                }
            }
        }, 5000);
    }
    @Override
    protected void onResume() {
        //start service and play music
        checkUser();
        startService(new Intent(MainActivity.this, SoundService.class));
        super.onResume();
    }
}