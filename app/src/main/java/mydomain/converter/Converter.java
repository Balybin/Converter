package mydomain.converter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Converter extends AppCompatActivity {

    Map<String, String> currencyData = new HashMap<String, String>();
    ArrayList<String> currencyList = new ArrayList<String>();
    boolean DataStatus = false;

    //ArrayList<Map<String,String>> currencyData = new ArrayList<Map<String,String>>;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MyTag", "onStart()");
        setContentView(R.layout.activity_converter);
        final Spinner currencyFrom = (Spinner) findViewById(R.id.currencyFrom);
        final Spinner currencyTo = (Spinner) findViewById(R.id.currencyTo);
        final EditText inputField = (EditText) findViewById(R.id.inputValue);
        final TextView resultValue = (TextView) findViewById(R.id.resultValue);
        final Button convertButton = (Button) findViewById(R.id.convertButton);

        View.OnClickListener oclConvertButton = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //URL url = new URL("https://free.currencyconverterapi.com/api/v6/convert?q=RUB_BTC&compact=ultra")
                try {
                    String url = "https://free.currencyconverterapi.com/api/v6/convert?q=" +
                            currencyData.get(currencyFrom.getSelectedItem()) + "_"
                            + currencyData.get(currencyTo.getSelectedItem()) + "&compact=ultra&apiKey=973c6ac039f7d47fbd2f";
                    NetworkManager networkManager = new NetworkManager();
                    String inputLine = networkManager.makeRequest(Converter.this, url);
                    inputLine = inputLine.substring(inputLine.indexOf(":") + 1, inputLine.length() - 1);
                    double coef = Double.parseDouble(inputLine);
                    double number = Double.parseDouble(inputField.getText().toString());
                    resultValue.setText(String.valueOf(number * coef));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        convertButton.setOnClickListener(oclConvertButton);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, currencyList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencyFrom.setAdapter(adapter);
        currencyTo.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MyTag", "onResume()");
        while(!DataStatus) {
            try {
                int index = -1;
                int counter = 0;
                String url = "http://free.currencyconverterapi.com/api/v6/currencies?apiKey=973c6ac039f7d47fbd2f";
                NetworkManager networkManager = new NetworkManager();
                String inputLine = networkManager.makeRequest(this, url);
                if (inputLine == null) DataStatus = false;
                else {
                    index = inputLine.indexOf("currencyName");
                    DataStatus = true;
                }
                String buf;
                while (index != -1) {
                    inputLine = inputLine.substring(index + 15);
                    index = inputLine.indexOf("\"");
                    buf = inputLine.substring(0, index);
                    currencyList.add(buf);
                    index = inputLine.indexOf("id");
                    inputLine = inputLine.substring(index + 5);
                    index = inputLine.indexOf("\"");
                    buf = inputLine.substring(0, index);
                    currencyData.put(currencyList.get(counter), buf);
                    index = inputLine.indexOf("currencyName");
                    counter++;
                }
                Collections.sort(currencyList);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i("MyTag", "onPause");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.i("MyTag", "onStop");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i("MyTag", "onDestroy");
    }
}
