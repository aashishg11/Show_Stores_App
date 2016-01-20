package com.aashishgodambe.android.showstores;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.aashishgodambe.android.showstores.Adapter.AdapterItem;
import com.aashishgodambe.android.showstores.Model.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    ArrayList<Item> listItems = new ArrayList<>();
    JSONObject object;
    RecyclerView mRecyclerView;
    AdapterItem mAdapterItem;
    private final String LOG_TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get reference to the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // JSON parsing is done in non UI thread.
        ListItemLoaderTask listItemLoaderTask = new ListItemLoaderTask();

        // Start the JSON parsing
        listItemLoaderTask.execute(loadJSONFromAsset());

        // Handle the recycler view item clicks.
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                String latitude = ""+listItems.get(position).getLatitude();
                String longitude = ""+listItems.get(position).getLongitude();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                // Generate a URL with specified latitude and longitude and label for a marker.
                String URL = "geo:0,0?q="+latitude+","+longitude+"(Marker)";
                intent.setData(Uri.parse(URL));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                // Check if there is an activity to handle the intent. IF TRUE, there is atleast one activity who can handle
                PackageManager packageManager = getPackageManager();
                List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
                boolean isIntentSafe = activities.size() > 0;

                // If true launch the intent
                if(isIntentSafe) {
                    startActivity(intent);
                }
            }
        }));

    }

    public String loadJSONFromAsset(){
        String JSONString = null;

        try {
            // Open the inputstream from assets folder.
            InputStream inputStream = getAssets().open("newspaper_locations.json");

            int sizeOfJSONFile = inputStream.available();

            // Array to store all data
            byte[] buffer = new byte[sizeOfJSONFile];

            // Reading data into array from file
            inputStream.read(buffer);

            // Close the input stream
            inputStream.close();

            JSONString = new String(buffer,"UTF-8");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return JSONString;
    }

    private class ListItemLoaderTask extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... params) {
            Log.d(LOG_TAG,"doInBackground called");

            // Get the JSON file and store it in an array list of class Items.
            try {
                object = new JSONObject(params[0]);

                // Get the locations array.
                JSONArray locations = object.getJSONObject("geodata").getJSONArray("locations");

                for(int i = 0 ; i < locations.length() ; i++ ){

                    Item item = new Item();

                    // Get style string
                    String style = locations.getJSONObject(i).getString("style");
                    item.setStyle(style);

                    // Get latitude
                    double latitude = locations.getJSONObject(i).getDouble("lat");
                    item.setLatitude(latitude);

                    // Get longitude
                    double longitude = locations.getJSONObject(i).getDouble("lng");
                    item.setLongitude(longitude);

                    // Get style string
                    String details = locations.getJSONObject(i).getString("details");
                    item.setDetails(details);

                    listItems.add(item);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(LOG_TAG,e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(LOG_TAG,"onPostExecute called");

            // Set the adapter and pass the item list
            mAdapterItem = new AdapterItem(listItems,getApplicationContext());
            mRecyclerView.setAdapter(mAdapterItem);

            // Specify the layout of recyclerview as linear layout manager.
            mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        }
    }


    // Recycler touch listener to check for touch events.
    public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            Log.d("Main Activity", "RecyclerTouchListener constructor invoked");
            this.clickListener = clickListener;

            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                // Pass the child view of recyclerView which is under the touch(where user tapped)
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    Log.d("Main Activity", "onSingleTapUp invoked");

                    View child = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onClick(child, mRecyclerView.getChildAdapterPosition(child));
                        Log.d("Main Activity", "onSingleTapUp invoked" + mRecyclerView.getChildAdapterPosition(child));
                    }

                    return true;
                }
            });

        }


        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }

            Log.d("Main Activity", "onInterceptTouchEvent invoked" + gestureDetector.onTouchEvent(e) + "" + e);
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            Log.d("Main Activity", "onTouchEvent invoked" + e);

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public static interface ClickListener {
        void onClick(View view, int position);
    }


}
