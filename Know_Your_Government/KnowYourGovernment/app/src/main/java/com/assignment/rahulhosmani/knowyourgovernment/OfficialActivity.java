package com.assignment.rahulhosmani.knowyourgovernment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class OfficialActivity extends AppCompatActivity {
    private Official official = new Official();
    private String locDetails;
    private TextView locationDetails, politicalPositionName, politicianName, partyName;
    private TextView addressDetails, phoneDetails, emailDetails, websiteDetails;
    private ImageView politicianPhoto, googlePlus, fb, twitter, youtube;
    private Map<String, String> channels = new HashMap<>();
    private ConstraintLayout cLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if(b != null)
        {
            official = (Official) b.get("officialDetails");
            locDetails = b.get("locationDetails").toString();
        }

        locationDetails = findViewById(R.id.locationDetails);
        politicalPositionName = findViewById(R.id.politicalPositionName);
        politicianName = findViewById(R.id.politicianName);
        partyName = findViewById(R.id.partyName);
        politicianPhoto = findViewById(R.id.politicianPhoto);
        googlePlus = findViewById(R.id.googlePlus);
        fb = findViewById(R.id.fb);
        twitter = findViewById(R.id.twitter);
        youtube = findViewById(R.id.youtube);
        addressDetails = findViewById(R.id.addressDetails);
        phoneDetails = findViewById(R.id.phoneDetails);
        emailDetails = findViewById(R.id.emailDetails);
        websiteDetails = findViewById(R.id.websiteDetails);
        cLayout = findViewById(R.id.cLayout);

        applyBackgroundColor(official.getPartyName());
        locationDetails.setText(locDetails);
        politicalPositionName.setText(official.getOfficeName());
        politicianName.setText(official.getName());
        partyName.setText("(" + official.getPartyName() + ")");
        if(official.getAddress().equalsIgnoreCase("No Data Provided")){
            addressDetails.setText(official.getAddress());
        } else
            addressDetails.setText(official.getAddress() + "\n" + official.getCity() + ", " + official.getState() + " " + official.getZip());
        phoneDetails.setText(official.getPhoneNo());
        emailDetails.setText(official.getEmail());
        websiteDetails.setText(official.getUrls());
        channels = official.getChannels();
        checkForAvailableChannels(channels);
        final String imageUrl = official.getPhotoUrl();

        setImage(imageUrl);

        Linkify.addLinks(addressDetails , Linkify.MAP_ADDRESSES);
        Linkify.addLinks(phoneDetails, Linkify.PHONE_NUMBERS);
        Linkify.addLinks(emailDetails, Linkify.EMAIL_ADDRESSES);
        Linkify.addLinks(websiteDetails, Linkify.WEB_URLS);
        addressDetails.setLinkTextColor(Color.WHITE);
        phoneDetails.setLinkTextColor(Color.WHITE);
        emailDetails.setLinkTextColor(Color.WHITE);
        websiteDetails.setLinkTextColor(Color.WHITE);
    }

    private void applyBackgroundColor(String partyName) {
        if(partyName.equalsIgnoreCase("Republican")){
            cLayout.setBackgroundColor(Color.RED);
        } else if(partyName.equalsIgnoreCase("Democratic") || partyName.equalsIgnoreCase("Democrat")){
            cLayout.setBackgroundColor(Color.BLUE);
        } else {
            cLayout.setBackgroundColor(Color.BLACK);
        }
    }

    public void googlePlusClicked(View v) {
        Intent intent = null;
        String gPlusId = channels.get("GooglePlus");
        try {
            // open Google plus app if installed
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus", "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", gPlusId );
            startActivity(intent);
        } catch (Exception e) {
            // No Google plus, open in browser
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + gPlusId)));
        }
    }



    private void checkForAvailableChannels(Map<String, String> channels) {
        googlePlus.setVisibility(View.GONE);
        fb.setVisibility(View.GONE);
        twitter.setVisibility(View.GONE);
        youtube.setVisibility(View.GONE);

        for(Map.Entry key : channels.entrySet()){
            if(key.getValue() != null){
                if(key.getKey().equals("GooglePlus")){
                    googlePlus.setVisibility(View.VISIBLE);
                }else if(key.getKey().equals("Facebook")){
                    fb.setVisibility(View.VISIBLE);
                }else if(key.getKey().equals("Twitter")){
                    twitter.setVisibility(View.VISIBLE);
                }else {
                    youtube.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void twitterClicked(View v) {
        Intent intent = null;
        String twitterId = channels.get("Twitter");
        try {
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + twitterId));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + twitterId)); } startActivity(intent);
    }

    public void facebookClicked(View v) {
        String fbId = channels.get("Facebook");
        String FACEBOOK_URL = "https://www.facebook.com/" + fbId;
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) {
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else {
                urlToUse = "fb://page/" + channels.get("Facebook");
            }
        } catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL;
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }



    public void youTubeClicked(View v) {
        Intent intent = null;
        String youtubeId = channels.get("YouTube");
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + youtubeId ));
            startActivity(intent);
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + youtubeId)));
        }
    }

    public void onImageClick(View v){
        if(official.getPhotoUrl().equalsIgnoreCase("No Data Provided")){
            return;
        }
        Intent intent = new Intent(OfficialActivity.this, PhotoActivity.class);
        intent.putExtra("officialDetails", official);
        intent.putExtra("locationDetails", locationDetails.getText().toString());
        startActivity(intent);
    }

    public void setImage(final String imageUrl) {
        if (!imageUrl.equalsIgnoreCase("No Data Provided")) {
            Picasso picasso = new Picasso.Builder(this).build();
            picasso.load(imageUrl)
                    .error(R.drawable.missingimage)
                    .placeholder(R.drawable.placeholder)
                    .into(politicianPhoto);
        } else {
            politicianPhoto.setImageResource(R.drawable.missingimage);
        }
    }
}
