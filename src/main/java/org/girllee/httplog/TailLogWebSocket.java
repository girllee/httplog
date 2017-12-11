package org.girllee.httplog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * A implements of tail command which dependent on tail command of
 *
 * @author Asin Liu
 * @version 1.5
 * @since 1.0
 */

@ServerEndpoint("/log/{filePath}/{fileName}")
public class TailLogWebSocket {

    private static Logger LOGGER = LoggerFactory.getLogger(TailLogWebSocket.class);
    private static final String CONFIG_PROPERTIES_FILE = "httplog.properties";
    private Process process;
    private InputStream inputStream;
    private static int rowsPerReq = 100;

    private static String logFileHome = "";

    static {
        LOGGER.info("=====================================");
        LOGGER.info("\tStarting the webSocket now...");
        LOGGER.info("=====================================");

        String confFilePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        LOGGER.info("conf file location is :{}", confFilePath);
        try {

            InputStream is = new FileInputStream(confFilePath + CONFIG_PROPERTIES_FILE);
            Properties properties = new Properties();
            properties.load(is);
            logFileHome = properties.getProperty("httplog.logFileHome");
            rowsPerReq = Integer.valueOf(properties.getProperty("httplog.linePerRequest"));

            LOGGER.info("logFileHome is:{}", logFileHome);
            LOGGER.info("linePerRequest is:{}", rowsPerReq);

        } catch (Exception e) {
            LOGGER.info("初始化配置时出现错误", e);
        }


        //InputStream is = ClassLoader.getSystemResourceAsStream(CONFIG_PROPERTIES_FILE);

    }

    /**
     * 新的WebSocket请求开启
     *
     * @param filePath 页面传入的文件目录名称
     * @param fileName 页面传入的文件名称
     * @param session  webSocket session
     */
    @OnOpen
    public void onOpen(@PathParam("filePath") String filePath, @PathParam("fileName") String fileName, Session session) {

        if(filePath.contains(".")){
            filePath = filePath.replace('.','/');
        }
        if (!filePath.endsWith("/")) {
            filePath +="/";
        }
        //System.out.println(filePath);
        String file = logFileHome + "/" + filePath + fileName;
        File f = new File(file);
        if (!f.exists()) {
            try {
                session.getBasicRemote().sendText("01");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (!f.canRead()) {
            try {
                session.getBasicRemote().sendText("02");
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            String command = "tail -fn " + rowsPerReq + " " + file;
            process = Runtime.getRuntime().exec(command);
            inputStream = process.getInputStream();
            Runnable runnable = new TailLogThread(inputStream, session);
            (new Thread(runnable)).start();
        } catch (IOException e) {
            LOGGER.error("Exception occurred on onOpen method:{}", e.getMessage(), e);
        }
    }


    /**
     * @param message
     */
    @OnMessage
    public void onMessage(String message) {
        LOGGER.debug("onMessage :{}", message);
    }

    /**
     * WebSocket请求关闭
     * 浏览器关闭后，会自动进入到onClose()方法中，将inputStream和process关闭和销毁
     */
    @OnClose
    public void onClose() {
        try {
            if (inputStream != null) {
                inputStream.close();
                inputStream = null;
                LOGGER.info("Input Stream close successful");
            }
            if (process != null) {
                process.destroy();
                process = null;
                LOGGER.info("process destroy successful");
            }
        } catch (Exception e) {
            LOGGER.info("Exception found onClose(),{}", e.getMessage(), e);
        }
    }

    @OnError
    public void onError(Throwable ex, Session session) {
        try {
            if (ex != null && ex.getMessage() != null) {
                LOGGER.error("", ex.getMessage());
                session.getBasicRemote().sendText(ex.getMessage());
            } else {
                session.getBasicRemote().sendText("Error occurred, the err msg is null");
            }
        } catch (Exception e) {
            LOGGER.error("Send error message to client error {}", e.getMessage(), e);
        }
    }

}
