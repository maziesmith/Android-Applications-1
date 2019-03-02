package com.assignment.rahulhosmani.knowyourgovernment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";

    private Locator locator;
    private RecyclerView listOfPoliticalOfficial;
    private OfficialsListViewAdapter viewAdapter;
    private TextView noConnectionMsgTitle;
    private TextView noConnectionInfo;

    private String NO_DATA_FOR_LOCATION = "No Data For Location";


    private TextView locationDetails;
    public List<Official> officialsObjList = new ArrayList<>();
    private CivicInfoDownloader asyncGetCivicDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listOfPoliticalOfficial = findViewById(R.id.listOfPoliticalOfficial);
        locationDetails = findViewById(R.id.locationDetails);
        noConnectionMsgTitle = findViewById(R.id.noConnectionMsgTitle);
        noConnectionInfo = findViewById(R.id.noConnectionInfo);

        viewAdapter = new OfficialsListViewAdapter(this.officialsObjList, this);
        listOfPoliticalOfficial.setAdapter(viewAdapter);
        listOfPoliticalOfficial.setLayoutManager(new LinearLayoutManager(this));

        //Check for Internet Connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isConnectedToWiFi = networkInfo.isConnected();
        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isConnectedToMobile = networkInfo.isConnected();

        if(!isConnectedToWiFi && !isConnectedToMobile){
            locationDetails.setText(NO_DATA_FOR_LOCATION);
            listOfPoliticalOfficial.setVisibility(View.GONE);
            noConnectionMsgTitle.setVisibility(View.VISIBLE);
            noConnectionInfo.setVisibility(View.VISIBLE);
        } else {
            listOfPoliticalOfficial.setVisibility(View.VISIBLE);
            noConnectionMsgTitle.setVisibility(View.GONE);
            noConnectionInfo.setVisibility(View.GONE);
            locator = new Locator(this);
        }
    }


    @Override
    public void onClick(View v) {
        int pos = listOfPoliticalOfficial.getChildLayoutPosition(v);
        Official o = officialsObjList.get(pos);
        Intent intent = new Intent(MainActivity.this, OfficialActivity.class);
        intent.putExtra("officialDetails", o);
        intent.putExtra("locationDetails", locationDetails.getText().toString());
        startActivity(intent);
    }

    public void updateList(Object []obj) {
        String locDetails = obj[1].toString();
        officialsObjList.addAll((List<Official>) obj[0]);
        listOfPoliticalOfficial.setVisibility(View.VISIBLE);
        noConnectionMsgTitle.setVisibility(View.GONE);
        noConnectionInfo.setVisibility(View.GONE);
        locationDetails.setText(locDetails);
        viewAdapter.notifyDataSetChanged();
     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch(menuItem.getItemId()){
            case R.id.aboutPage:
                Intent i =new Intent(MainActivity.this, AboutActivity.class);
                startActivity(i);
                break;
            case R.id.locationFinder:
                if(isConnectedToNetwork()) {
                    openDialogToAddStock();
                }
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void openDialogToAddStock(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.location_details_dialog, null);
        final EditText locationDetails = (EditText) dialogView.findViewById(R.id.locationDetails);

        dialogBuilder.setView(dialogView);
        dialogBuilder.setMessage("Enter a City and State or a Zip Code:");
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String address = locationDetails.getText().toString();
                getTheCivicData(address);
                locationDetails.setText("");
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                locationDetails.setText("");
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void setData(double lat, double lon) {
        String address = doAddress(lat, lon);
        if(isConnectedToNetwork()) {
            getTheCivicData(address);
        } else{
            officialsObjList.clear();
            Object[] obj = new Object[2];
            obj[0] = officialsObjList;
            obj[1] = NO_DATA_FOR_LOCATION;
            updateList(obj);
        }
    }

    private void getTheCivicData(String address) {
        asyncGetCivicDetails = new CivicInfoDownloader(this, address, this.getString(R.string.api_key));
        asyncGetCivicDetails.execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        locator.setUpLocationManager();
                        locator.determineLocation();
                    } else {
                    }
                }else{
                    Toast.makeText(this, "Address cannot be acquired from provided latitude/longitude", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private String doAddress(double latitude, double longitude) {
        Log.d(TAG, "doAddress: Lat: " + latitude + ", Lon: " + longitude);
        List<Address> addresses = null;
        for (int times = 0; times < 3; times++) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);

                StringBuilder sb = new StringBuilder();
                if(addresses.size()>0) {
                    Address a = addresses.get(0);
                    return a.getPostalCode();
                }else{
                    return "";
                }
            } catch (IOException e) {
                Toast.makeText(this, "Address cannot be acquired from provided latitude/longitude", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "doAddress: " + e.getMessage());
            }
            Toast.makeText(this, "GeoCoder service is slow - please wait", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "GeoCoder service timed out - please try again", Toast.LENGTH_LONG).show();
        return null;
    }

    public void noLocationAvailable() {
        locationDetails.setText(NO_DATA_FOR_LOCATION);
        listOfPoliticalOfficial.setVisibility(View.GONE);
        noConnectionMsgTitle.setVisibility(View.VISIBLE);
        noConnectionInfo.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        locator.shutdown();
        super.onDestroy();
    }

    public boolean isConnectedToNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("address", locationDetails.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String address = savedInstanceState.getString("address").toString();
        getTheCivicData(address);
    }
}


