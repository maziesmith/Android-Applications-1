package com.assignment.rahulhosmani.temperatureconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText inputTextTemperature;
    private EditText outputTextTemperature;
    private RadioButton radioFahrenheitToCelsius;
    private RadioButton radioCelsiusToFahrenheit;
    private TextView textViewHistory;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Binding variables to the user interface elements
        inputTextTemperature = findViewById(R.id.editTextInput);
        outputTextTemperature = findViewById(R.id.editTextOutput);
        radioCelsiusToFahrenheit = findViewById(R.id.radioBtnC2F);
        radioFahrenheitToCelsius = findViewById(R.id.radioBtnF2C);
        textViewHistory = findViewById(R.id.textViewHistory);

        //Setting Scrolling method to TextViewHistory
        textViewHistory.setMovementMethod(new ScrollingMovementMethod());
        Log.d(TAG, "onCreate: Variables are bound to user interface elements");
    }

    public void convertButtonClicked(View v){

        double doubleInputTemp, doubleOutputTemp;
        String strInput, strOutput;
        String strPreviousHistory = textViewHistory.getText().toString();

        if(!inputTextTemperature.getText().toString().isEmpty()){
            Toast.makeText(this, "Converted", Toast.LENGTH_SHORT).show();
            doubleInputTemp = Double.valueOf(inputTextTemperature.getText().toString());
            strInput = String.format("%.1f",doubleInputTemp);

            if(radioFahrenheitToCelsius.isChecked()){
                //Converting Fahrenheit to Celsius
                Log.d(TAG, "convertButtonClicked: Converting Fahrenheit to Celsius");
                doubleOutputTemp = (doubleInputTemp - 32.0) / 1.8;
                strOutput = String.format("%.1f", doubleOutputTemp);
                outputTextTemperature.setText(strOutput);
                textViewHistory.setText("Fahrenheit to Celsius: " + strInput + " \u2794 " + strOutput + "\n" + strPreviousHistory );
            }

            if(radioCelsiusToFahrenheit.isChecked()){
                //Converting Celsius to Fahrenheit
                Log.d(TAG, "convertButtonClicked: Converting Celsius to Fahrenheit");
                doubleOutputTemp = (doubleInputTemp * 1.8) + 32;
                strOutput = String.format("%.1f", doubleOutputTemp);
                outputTextTemperature.setText(strOutput);
                textViewHistory.setText("Celsius to Fahrenheit: " + strInput + " \u2794 " + strOutput + "\n" + strPreviousHistory );
            }

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("HISTORY", textViewHistory.getText().toString());
        Log.d(TAG, "onSaveInstanceState: Saving History from TextView in the Bundle");
        // Calling Super Last
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Calling Super First
        Log.d(TAG, "onRestoreInstanceState: Restoring History into the TextView");
        super.onRestoreInstanceState(savedInstanceState);
        textViewHistory.setText(savedInstanceState.getString("HISTORY"));
    }
}
