package sagar.musicshare.utils.asynctasks;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import sagar.musicshare.Globals;

/**
 * Created by Sagar on 18-05-2016.
 */
public class SongReceiver extends AsyncTask<File, Void, String> {

    private int port = Globals.SONG_PORT;
    private String serverIp=null;
    Callback callback = null;
    public final static int FILE_SIZE = 6022386;

    public SongReceiver(String serverIp, Callback callback){
        this.serverIp = serverIp;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(File[] params) {
        receiveDoc();
        return null;
    }

    public void receiveDoc(){
        int bytesRead;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket sock = null;
        try {
            Log.e("My Tag", "Connecting...");
            sock = new Socket(serverIp, port);
            Log.e("My Tag", "Connected...");

            //Receive Song
            Log.e("My Tag", "Receiving Song");
            byte [] mybytearray  = new byte [FILE_SIZE];
            InputStream is = sock.getInputStream();

            File f = new File(Globals.APP_DIRECTORY);
            if(!f.exists())
                f.mkdir();
            fos = new FileOutputStream(Globals.TEMP_FILE_PATH);

            bos = new BufferedOutputStream(fos);
            bytesRead = is.read(mybytearray,0,mybytearray.length);
            current = bytesRead;

            do {
                bytesRead =
                        is.read(mybytearray, current, (mybytearray.length-current));
                if(bytesRead >= 0) current += bytesRead;
                Log.e("My Tag", "Current New: "+bytesRead);
            } while(bytesRead > 0);

            bos.write(mybytearray, 0, current);
            bos.flush();
            Log.e("My Tag", "Song Received");
            //Receive Song

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) fos.close();
                if (bos != null) bos.close();
                if (sock != null) sock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPostExecute(String s) {
        callback.onSongReceive();
        super.onPostExecute(s);
    }

    public interface Callback{
        void onSongReceive();
    }
}