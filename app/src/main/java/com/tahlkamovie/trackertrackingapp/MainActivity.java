package com.tahlkamovie.trackertrackingapp;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btn1,btn2;
    TextView tv;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = (Button) findViewById(R.id.button);
        btn2 = (Button) findViewById(R.id.button2);
        tv = (TextView) findViewById(R.id.textView);

        database= FirebaseDatabase.getInstance();

        reference = database.getReference("Location_Updates");

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            return;

        }

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Score data=dataSnapshot.getValue(Score.class);

                        String latitude= data.getTeamName();
                        String longitude=data.getTeamScore();

                        Toast.makeText(MainActivity.this, "Data are showing below"+latitude+","+longitude , Toast.LENGTH_SHORT).show();

                        tv.setText("Lattitude is :-"+latitude+
                        "Longitude is :-"+longitude);

                        double sp=Double.parseDouble(latitude);
                        double xp=Double.parseDouble(longitude);

                        Geocoder geocoder = new Geocoder(MainActivity.this);
                        try {
                            List<Address> adr = geocoder.getFromLocation(sp, xp, 1);
                            String country = adr.get(0).getCountryName();

                            String locality = adr.get(0).getLocality();
                            String postalCode = adr.get(0).getPostalCode();
                            String address = adr.get(0).getAddressLine(0);

                            tv.append(country + "," + locality + "," + address + "," + postalCode);

                        }
                        catch (Exception e) {


                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });



        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String latitudes=tv.getText().toString();
                String longitudes=tv.getText().toString();

                String label="Current Location";
                String uriBegin = "geo:"+latitudes+","+longitudes;

                String query = latitudes + ","+longitudes + "("+label+")";

                String encodeQuery = Uri.encode(query);

                String uriString = uriBegin + "?q=" +encodeQuery + "&z=16";

                Uri uri = Uri.parse(uriString);

                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);

            }
        });


    }
}
