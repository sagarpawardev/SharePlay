package sagar.musicshare.utils.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
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
    private static ServerSocket servsock = null;
    private File fileToSend = null;
    private static int reqCounts;

    public SongSender(Context context){
        this.context = context;

        if(servsock != null)
            try {
                servsock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

        Log.e("My Tag", "Starting Port....");
        try {
            servsock = new ServerSocket(port);
            Log.e("My Tag", "Port Opened....");

            while (true) {
                System.out.println("Waiting for Requests...");
                try {
                    sock = servsock.accept();

                    Log.e("My Tag","Accepted connection : " + sock);
                    //Send Song
                    byte [] mybytearray  = new byte [(int)myFile.length()];
                    fis = new FileInputStream(myFile);
                    bis = new BufferedInputStream(fis);
                    bis.read(mybytearray,0,mybytearray.length);
                    os = sock.getOutputStream();
                    System.out.println("Sending " + myFile.getName() + "(" + mybytearray.length + " bytes)");
                    os.write(mybytearray, 0, mybytearray.length);
                    os.flush();
                    //Send Song
                    Log.e("My Tag", "Song Sent");

                    publishProgress();
                }
                finally {
                    if (bis != null) bis.close();
                    if (os != null) os.close();
                    if (sock!=null) sock.close();
                }

                Log.e("My Tag", "Number of Requests: "+reqCounts++);
            }
        } catch (FileNotFoundException e) {
            Log.e("My Tag Socket", e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("My Tag Socket", e.getMessage());
        } finally {
            if (servsock != null) try {
                servsock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
