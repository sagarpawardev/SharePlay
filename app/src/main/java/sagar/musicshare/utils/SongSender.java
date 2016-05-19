package sagar.musicshare.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import sagar.musicshare.Globals;

/**
 * Created by Sagar on 18-05-2016.
 */
public class SongSender extends AsyncTask<File, Void, String> {

    private int port = Globals.SONG_PORT;
    private Context context;
    private Callback callback;
    private ServerSocket servsock = null;
    private File fileToSend = null;

    public SongSender(Context context, Callback callback){
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(File... params) {
        fileToSend = params[0];
        sendDoc(params[0]);
        return null;
    }

    void sendDoc(File file){
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        Socket sock = null;
        File myFile = file;
        try {
            servsock = new ServerSocket(port);

            while (true) {
                System.out.println("Waiting...");
                try {
                    sock = servsock.accept();

                    Log.e("MyTag","Accepted connection : " + sock);
                    // send file
                    byte [] mybytearray  = new byte [(int)myFile.length()];
                    fis = new FileInputStream(myFile);
                    bis = new BufferedInputStream(fis);
                    bis.read(mybytearray,0,mybytearray.length);
                    os = sock.getOutputStream();
                    System.out.println("Sending " + myFile.getName() + "(" + mybytearray.length + " bytes)");
                    os.write(mybytearray, 0, mybytearray.length);
                    os.flush();
                    System.out.println("Done.");

                    publishProgress();
                }
                finally {
                    if (bis != null) bis.close();
                    if (os != null) os.close();
                    if (sock!=null) sock.close();
                }
            }
        } catch (FileNotFoundException e) {
            Log.e("MyTag", e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MyTag", e.getMessage());
        } finally {
            if (servsock != null) try {
                servsock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPostExecute(String s) {
        callback.onSongSent(true, fileToSend);
        super.onProgressUpdate();
    }

    public interface Callback{
        void onSongSent(boolean result, File file);
    }
}
