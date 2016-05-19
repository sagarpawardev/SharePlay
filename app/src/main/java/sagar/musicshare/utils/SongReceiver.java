package sagar.musicshare.utils;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    @Override
    protected void onPostExecute(String s) {
        callback.onSongReceive();
        super.onPostExecute(s);
    }

    public void receiveDoc(){
        int bytesRead;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket sock = null;
        try {
            sock = new Socket(serverIp, port);
            Log.e("MyTag", "Connecting...");

            // receive file
            byte [] mybytearray  = new byte [FILE_SIZE];
            InputStream is = sock.getInputStream();

            File f = new File(Globals.APP_DIRECTORY);
            if(!f.exists())
                f.mkdir();

            fos = new FileOutputStream(Globals.APP_DIRECTORY + "/" + Globals.TEMP_FILE_NAME);

            bos = new BufferedOutputStream(fos);
            bytesRead = is.read(mybytearray,0,mybytearray.length);
            current = bytesRead;

            do {
                bytesRead =
                        is.read(mybytearray, current, (mybytearray.length-current));
                if(bytesRead >= 0) current += bytesRead;
            } while(bytesRead > -1);

            bos.write(mybytearray, 0, current);
            bos.flush();
            Log.e("myTag","File temp.mp3 downloaded (" + current + " bytes read)");
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

    public interface Callback{
        void onSongReceive();
    }
}