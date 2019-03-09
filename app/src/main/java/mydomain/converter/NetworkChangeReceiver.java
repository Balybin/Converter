package mydomain.converter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class NetworkChangeReceiver extends BroadcastReceiver {
    boolean isDataLoaded;
    Map<String, String> currencyData;
    ArrayList<String> currencyList;

    public NetworkChangeReceiver(boolean isDtaLoaded, Map<String, String> currencyData, ArrayList<String> currencyList) {
        this.isDataLoaded = isDtaLoaded;
        this.currencyData = currencyData;
        this.currencyList = currencyList;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (isDataLoaded) return;
            Requestor requestor = new Requestor();
            requestor.execute("http://free.currencyconverterapi.com/api/v6/currencies?apiKey=973c6ac039f7d47fbd2f");
            String inputLine = requestor.get();
            int index = -1;
            int counter = 0;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}