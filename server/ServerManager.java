package server;

import server.Controller;


import interfaces.ISession;
import network.Network;
import network.MyKeyHandler;
import network.MySession;
import network.MessageSendCollect;
import interfaces.ISessionAcceptHandler;
import java.util.*;
import utils.Logger;
import utils.TimeUtil;


public class ServerManager {
    
    private static ServerManager instance;
    public static final Map<Object, Object> CLIENTS = new HashMap<>();

    public static String timeStart;
    public static boolean isRunning;
    public static String NAME_SERVER = "Back Box NRO"; // Tên Máy Chủ
    public static int PORT = 14445; // Port

    public static void main(String[] args){
        timeStart = TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss");
        System.out.println("=================Khởi động máy chủ " + NAME_SERVER + " lúc: " + timeStart + "=================");
        ServerManager.gI().run();
    }

    public static ServerManager gI(){
        if (instance == null) {
            instance = new ServerManager();
            instance.initServer();
        }
        return instance;
    }

    public void initServer(){
        System.out.println("Initializing server...");
        

    }

    public void run(){
        isRunning = true;
        activeServerSocket();


    }


    /// support  MEthod 
    /// 

    public void activeServerSocket() {
        try {
            Network.gI().init().setAcceptHandler(new ISessionAcceptHandler() {
                @Override
                public void sessionInit(ISession is) {
                    if (!canConnectWithIp(is.getIP())) {
                        is.disconnect();
                        return;
                    }
                    is.setMessageHandler(Controller.gI())
                            .setSendCollect(new MessageSendCollect())
                            .setKeyHandler(new MyKeyHandler())
                            .startCollect().startQueueHandler();
                }

                @Override
                public void sessionDisconnect(ISession session) {
                    // Client.gI().kickSession((MySession) session);
                    // disconnect((MySession) session);
                }
            }).setTypeSessionClone(MySession.class)
              .setDoSomeThingWhenClose(() -> {
                  Logger.error("SERVER CLOSE\n");
                  System.exit(0);
              })
              .start(PORT);
        } catch (Exception e) {
            Logger.error("Lỗi khi khởi động máy chủ: " + e.getMessage());
        }
    }
    private boolean canConnectWithIp(String ipAddress) {
        Object o = CLIENTS.get(ipAddress);
        if (o == null) {
            CLIENTS.put(ipAddress, 1);
            return true;
        } else {
            int n = Integer.parseInt(String.valueOf(o));

            // set max PEER / IP
            if (n < 155555) {
                n++;
                CLIENTS.put(ipAddress, n);
                return true;
            } else {
                return false;
            }
        }
    }
    // public void disconnect(MySession session) {
    //     Object o = CLIENTS.get(session.getIP());
    //     if (o != null) {
    //         int n = Integer.parseInt(String.valueOf(o));
    //         n--;
    //         if (n < 0) {
    //             n = 0;
    //         }
    //         CLIENTS.put(session.getIP(), n);
    //     }
    // } 
}
