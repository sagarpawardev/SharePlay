package sagar.musicshare.utils.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;

import sagar.musicshare.Globals;
import sagar.musicshare.utils.SyncHelper;

/**
 * Created by Sagar on 19-05-2016.
 */
public class TimestampReceiver extends AsyncTask {

    String serverIp;
    Callback callback;
    int port = Globals.SYNC_PORT;
    long timestamp;
    int seekPosition;

    public  TimestampReceiver(String serverIp, Callback callback){
        this.serverIp = serverIp;
        this.callback = callback;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        receiveTimestamp();
        return null;
    }

    public void receiveTimestamp(){
        int current = 0;
        Socket sock = null;
        try {
            sock = new Socket(serverIp, port);
            Log.e("My Tag", "Connecting...");

            Log.e("My Tag", "Receiving Timestamp");
            //Reading DTS
            InputStream is = sock.getInputStream();

            //Receiving Json
            DataInputStream dis = new DataInputStream(is);
            String result  = dis.readUTF();
            //-- Receiving JSon

            /*BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String s = "";
            String temp = "";
            do{
                s += temp;
                temp = br.readLine();
            }while(temp != null);*/
            //-- Reading DTS
            Log.e("My Tag", "Received String: "+result);

            //Decoding JSON
            JSONObject mainObject = null;
            try {
                mainObject = new JSONObject(result);
                timestamp = mainObject.getLong(Globals.TIMESTAMP_JSON_KEY);
                seekPosition = mainObject.getInt(Globals.SEEK_POSITION_JSON_KEY);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //-- Decoding JSON

            Log.e("My Tag", "Timestamp Received: Timestamp="+new Timestamp(timestamp)+" SeekPosition="+seekPosition);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (sock != null) sock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        callback.onTimestampReceive(timestamp, seekPosition);
        super.onPostExecute(o);
    }

    public interface Callback{
        void onTimestampReceive(long timestamp, int seekPosition);
    }
}
