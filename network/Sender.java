package network;

import java.net.Socket;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import lombok.NonNull;
import interfaces.IMessageSendCollect;
import interfaces.ISession;

public final class Sender implements Runnable {

    private ISession session;
    private BlockingDeque<Message> messages;
    private DataOutputStream dos;
    private IMessageSendCollect sendCollect;

    public Sender(@NonNull ISession session, @NonNull Socket socket) {
        try {
            this.session = session;
            this.messages = new LinkedBlockingDeque<>();
            this.setSocket(socket);
        } catch (Exception ignored) {
        }
    }

    public Sender setSocket(@NonNull Socket socket) {
        try {
            this.dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ignored) {
        }
        return this;
    }

    @Override
    public void run() {
        try {
            while (session.isConnected()) {
                while (!messages.isEmpty()) {
                    Message message = messages.poll(5, TimeUnit.SECONDS);
                    if (message != null) {
                        doSendMessage(message);
                        message.cleanup();
                    }
                }
                TimeUnit.MILLISECONDS.sleep(33); //~30FPS
            }
        } catch (Exception ignored) {
        }
    }

    public synchronized void doSendMessage(Message message) throws Exception {
        this.sendCollect.doSendMessage(this.session, this.dos, message);
    }

    public void sendMessage(Message msg) {
        try {
            if (session.isConnected()) {
                messages.add(msg);
            }
        } catch (Exception ignored) {
        }
    }

    public void setSend(IMessageSendCollect sendCollect) {
        this.sendCollect = sendCollect;
    }

    public void close() {
        this.messages.clear();
        if (this.dos != null) {
            try {
                this.dos.close();
            } catch (IOException ignored) {
            }
        }
    }

    public void dispose() {
        this.session = null;
        this.messages = null;
        this.sendCollect = null;
        this.dos = null;
    }
}
