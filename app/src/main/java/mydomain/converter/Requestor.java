package mydomain.converter;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Requestor extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... Params)
    {
        try {
            //URL url = new URL("https://free.currencyconverterapi.com/api/v6/currencies");
            URL url = new URL(Params[0]);
            URLConnection connection = url.openConnection();
            BufferedReader data = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));
            String inputLine;// = new String();
            inputLine = data.readLine();
            return inputLine;
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
