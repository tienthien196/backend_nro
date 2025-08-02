package interfaces;

import network.Message;

public interface IMessageHandler {

    void onMessage(final ISession p0, final Message p1) throws Exception;
}
