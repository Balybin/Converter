package mydomain.converter;

import android.content.IntentFilter;
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
    boolean isDataLoaded = false;

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
                isDataLoaded = false;
            }
        };
        convertButton.setOnClickListener(oclConvertButton);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Spinner currencyFrom = (Spinner) findViewById(R.id.currencyFrom);
        final Spinner currencyTo = (Spinner) findViewById(R.id.currencyTo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, currencyList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Log.i("MyTag", "onResume()");
        tryMakeRequest();
        if (!isDataLoaded) {
            initBroadcastReceiver(currencyFrom, currencyTo);
        }
        currencyFrom.setAdapter(adapter);
        currencyTo.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("MyTag", "onPause");
        //вот тут, если данные не загружены, надо отменить ожидание загрузки данных
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("MyTag", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MyTag", "onDestroy");
    }

    private void initBroadcastReceiver(Spinner currencyFrom, Spinner currencyTo) {
        //NetworkChangeReceiver receiver = new NetworkChangeReceiver(isDataLoaded, currencyData, currencyList);
        NetworkChangeReceiver receiver = new NetworkChangeReceiver(isDataLoaded, currencyData,currencyList,currencyFrom,currencyTo);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiver, filter);
    }

    private void tryMakeRequest() {
        try {
            int index = -1;
            int counter = 0;
            String url = "http://free.currencyconverterapi.com/api/v6/currencies?apiKey=973c6ac039f7d47fbd2f";
            NetworkManager networkManager = new NetworkManager();
            String inputLine = networkManager.makeRequest(this, url);
            if (inputLine == null) isDataLoaded = false;
            else index = inputLine.indexOf("currencyName");
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
            if (currencyList.size() != 0) isDataLoaded = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
