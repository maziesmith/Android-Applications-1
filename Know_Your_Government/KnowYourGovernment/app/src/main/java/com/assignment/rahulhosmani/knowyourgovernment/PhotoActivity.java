package com.assignment.rahulhosmani.knowyourgovernment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {
    private Official official = new Official();
    private String locDetails;
    private ImageView fullSizeImage;
    private TextView locationDetails, politicalPositionName, politicianName;
    private ConstraintLayout cLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        fullSizeImage = findViewById(R.id.fullSizeImage);
        locationDetails = findViewById(R.id.locationDetails);
        politicalPositionName = findViewById(R.id.politicalPositionName);
        politicianName = findViewById(R.id.politicianName);
        cLayout = findViewById(R.id.cLayout);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if(b != null)
        {
            official = (Official) b.get("officialDetails");
            locDetails = b.get("locationDetails").toString();

            final String imageUrl = official.getPhotoUrl();
            setImage(imageUrl);
        }
        locationDetails.setText(locDetails);
        politicalPositionName.setText(official.getOfficeName());
        politicianName.setText(official.getName());
        applyBackgroundColor(official.getPartyName());
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

    private void setImage(final String imageUrl){
        if (imageUrl!= null) {
            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    final String changedUrl = imageUrl.replace("http:", "https:");
                    picasso.load(changedUrl)
                            .error(R.drawable.missingimage)
                            .placeholder(R.drawable.placeholder)
                            .into(fullSizeImage);
                }
            }).build();
            picasso.load(imageUrl)
                    .error(R.drawable.missingimage)
                    .placeholder(R.drawable.placeholder)
                    .into(fullSizeImage);
        } else {
            Picasso.get().load(imageUrl)
                    .error(R.drawable.missingimage)
                    .placeholder(R.drawable.placeholder)
                    .into(fullSizeImage);
        }
    }

}
