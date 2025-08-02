package network;

import network.Message;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import interfaces.ISession;
import interfaces.IMessageSendCollect;

public class MessageSendCollect implements IMessageSendCollect {

    private int curR;
    private int curW;

    public MessageSendCollect() {
        this.curR = 0;
        this.curW = 0;
    }

    @Override
    public Message readMessage(ISession session, DataInputStream dis) throws Exception {
        byte cmd = dis.readByte();
        if (session.sentKey()) {
            cmd = this.readKey(session, cmd);
        }
        int size;
        if (session.sentKey()) {
            final byte b1 = dis.readByte();
            final byte b2 = dis.readByte();
            size = ((this.readKey(session, b1) & 0xFF) << 8 | (this.readKey(session, b2) & 0xFF));
        } else {
            size = dis.readUnsignedShort();
        }
        final byte[] data = new byte[size];
        for (int len = 0, byteRead = 0; len != -1 && byteRead < size; byteRead += len) {
            len = dis.read(data, byteRead, size - byteRead);
            if (len > 0) {
            }
        }
        if (session.sentKey()) {
            for (int i = 0; i < data.length; ++i) {
                data[i] = this.readKey(session, data[i]);
            }
        }
        return new Message(cmd, data);
    }

    @Override
    public byte readKey(ISession session, byte b) {
        final byte i = (byte) ((session.getKey()[this.curR++] & 0xFF) ^ (b & 0xFF));
        if (this.curR >= session.getKey().length) {
            this.curR %= session.getKey().length;
        }
        return i;
    }

    @Override
    public void doSendMessage(ISession session, DataOutputStream dos, Message msg) throws Exception {
        try {
            final byte[] data = msg.getData();
            if (session.sentKey()) {
                final byte b = this.writeKey(session, msg.command);
                dos.writeByte(b);
            } else {
                dos.writeByte(msg.command);
            }
            if (data != null) {
                final int size = data.length;
                if (msg.command == -32 || msg.command == -66 || msg.command == -74 || msg.command == 11 || msg.command == -67 || msg.command == -87 || msg.command == 66) {
                    final byte b2 = this.writeKey(session, (byte) size);
                    dos.writeByte(b2 - 128);
                    final byte b3 = this.writeKey(session, (byte) (size >> 8));
                    dos.writeByte(b3 - 128);
                    final byte b4 = this.writeKey(session, (byte) (size >> 16));
                    dos.writeByte(b4 - 128);
                } else if (session.sentKey()) {
                    final int byte1 = this.writeKey(session, (byte) (size >> 8));
                    dos.writeByte(byte1);
                    final int byte2 = this.writeKey(session, (byte) (size & 0xFF));
                    dos.writeByte(byte2);
                } else {
                    dos.writeShort(size);
                }
                if (session.sentKey()) {
                    for (int i = 0; i < data.length; ++i) {
                        data[i] = this.writeKey(session, data[i]);
                    }
                }
                dos.write(data);
            } else {
                dos.writeShort(0);
            }
            dos.flush();
            msg.cleanup();
        } catch (IOException ex) {
        }
    }

    @Override
    public byte writeKey(final ISession session, final byte b) {
        final byte i = (byte) ((session.getKey()[this.curW++] & 0xFF) ^ (b & 0xFF));
        if (this.curW >= session.getKey().length) {
            this.curW %= session.getKey().length;
        }
        return i;
    }
}
