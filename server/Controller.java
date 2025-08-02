package server;

import interfaces.IMessageHandler;
import interfaces.ISession;
import network.Message;
import network.MySession;
import player.Player;
import utils.Logger;
public class Controller implements IMessageHandler {
    private int errors;

    private static Controller instance;

    public static Controller gI() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }
    public static String a = "";

    @Override
    public void onMessage(ISession s, Message _msg) {
        long st = System.currentTimeMillis();
        MySession _session = (MySession) s;
        Player player = null;
        if (_session.ipAddress != null && _session.ipAddress != a){
            a = _session.ipAddress;
            Logger.primaryln("=========================================================");
        }
        Logger.log("\nReceived message: " + _msg.command + " from " + _session.ipAddress);
    }
}
