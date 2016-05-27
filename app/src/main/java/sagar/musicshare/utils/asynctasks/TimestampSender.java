package sagar.musicshare.utils.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;

import sagar.musicshare.Globals;
import sagar.musicshare.ShareBucket;

/**
 * Created by Sagar on 19-05-2016.
 */
public class TimestampSender extends AsyncTask {


    ServerSocket servsock = null;
    final int port = Globals.SYNC_PORT;

    @Override
    protected Object doInBackground(Object[] params) {
        sendTimestamp();
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }

    void sendTimestamp(){

        BufferedInputStream bis = null;
        OutputStream os = null;
        Socket sock = null;
        try {
            servsock = new ServerSocket(port);

            while (true) {
                System.out.println("Waiting for TS requests...");
                try {
                    sock = servsock.accept();
                    Log.e("My Tag","Accepted connection : " + sock);
                    os = sock.getOutputStream();

                    //Request DTS and SeekPosition
                    long timeStamp = ShareBucket.getStartTimestamp();
                    int seekPosition = ShareBucket.getSeekPosition();
                    //-- Request DTS and SeekPosition

                    Log.e("My Tag", "Sending Time stamp "+new Timestamp(timeStamp));
                    //Convert DTS and SeekPosition to JSON
                    JSONObject mainJson = new JSONObject();
                    mainJson.put(Globals.TIMESTAMP_JSON_KEY, timeStamp);
                    mainJson.put(Globals.SEEK_POSITION_JSON_KEY, seekPosition);
                    //-- Convert DTS and SeekPosition to JSON

                    //Send JSON
                    /*PrintWriter writer = new PrintWriter(os, true);
                    writer.println(mainJson.toString());
                    os.flush();*/

                    DataOutputStream dos = new DataOutputStream(os);
                    dos.writeUTF(mainJson.toString());
                    os.flush();
                    //-- Send JSON


                    Log.e("My Tag", "Timestamp sent");

                    publishProgress();

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) bis.close();
                    if (os != null) os.close();
                    if (sock!=null) sock.close();
                }
            }
        } catch (FileNotFoundException e) {
            Log.e("My Tag", e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("My Tag", e.getMessage());
        } finally {
            if (servsock != null) try {
                servsock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
