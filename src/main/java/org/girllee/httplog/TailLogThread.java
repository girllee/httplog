package org.girllee.httplog;

import javax.websocket.Session;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by boot on 8/1/16.
 *
 * @author Asin Liu
 */
public class TailLogThread implements Runnable {

    private BufferedReader reader;
    private Session session;

    /**
     * Constructor
     *
     * @param in      InputStream where the message from.
     * @param session ws Session.
     */
    public TailLogThread(InputStream in, Session session) {
        this.reader = new BufferedReader(new InputStreamReader(in));
        this.session = session;
    }


    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p/>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    public void run() {
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (session.isOpen())
                    session.getBasicRemote().sendText(line);
                else
                    System.out.println("session is closed.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
