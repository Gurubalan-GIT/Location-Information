package a1.latitudeandlongitude;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import a1.test.R;

public class EnterCoordinates extends AppCompatActivity {
    private static EditText elatitude,elongitude;
    private String latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_coordinates);
        Button button=(Button)findViewById(R.id.clicker);
        elatitude=(EditText)findViewById(R.id.input1);
        elongitude=(EditText)findViewById(R.id.input);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latitude=elatitude.getText().toString();
                longitude=elongitude.getText().toString();
                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+latitude+","+longitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });
    }
}
