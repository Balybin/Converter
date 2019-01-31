package mydomain.converter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.net.*;
import java.util.*;
import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class Converter extends AppCompatActivity {

    Map<String, String> currencyData = new HashMap<String, String>();
    ArrayList<String> currencyList = new ArrayList<String>();

    //ArrayList<Map<String,String>> currencyData = new ArrayList<Map<String,String>>;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    URL url = new URL("https://free.currencyconverterapi.com/api/v6/convert?q=" +
                            currencyData.get(currencyFrom.getSelectedItem()) + "_"
                            + currencyData.get(currencyTo.getSelectedItem()) + "&compact=ultra");
                    Requestor requestor = new Requestor();
                    requestor.execute(url);
                    String inputLine = requestor.get();
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
        boolean NetworkStatus;
        do {
            try {
                NetworkStatus = false;
                URL url = new URL("https://free.currencyconverterapi.com/api/v6/currencies");
                Requestor requestor = new Requestor();
                requestor.execute(url);
                String inputLine = requestor.get();
                int counter = 0, index = inputLine.indexOf("currencyName");
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, currencyList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                currencyFrom.setAdapter(adapter);
                currencyTo.setAdapter(adapter);
                } catch (Exception e) {
                e.printStackTrace();
                NetworkStatus = true;
                AlertDialog.Builder ad = new AlertDialog.Builder(this);
                ad.setTitle("Ошибка!");
                ad.setMessage("Что-то не так с интернет-соединением");
                ad.setNegativeButton("Ок", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();}
                });
                ad.show();
            }
        } while (NetworkStatus);

        // findViewById(R.id.currencyFrom);
    }
}
