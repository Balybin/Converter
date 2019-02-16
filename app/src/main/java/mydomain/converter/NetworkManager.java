package mydomain.converter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkManager {
    Requestor requestor;// = new Requestor();
    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected())
            return true;
        else return false;
    }

    public static String makeRequest(Context context, String url){
        String result;
        if(isNetworkAvailable(context)) {
            Requestor requestor = new Requestor();
            requestor.execute(url);
            try {
                return requestor.get();
            }
            catch(Exception e)
                {
                    e.printStackTrace();
                }
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Что-то пошло не так");
            builder.setMessage("Отсуствует соединение с интернетом");
            builder.setCancelable(false);
            builder.setIcon(android.R.drawable.ic_dialog_info);
            builder.setPositiveButton("ОК",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            builder.show();
            result = null;
            return result;
        }
        return null;
    }
}
