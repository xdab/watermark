package mini.xdab.digital.util;

import lombok.NonNull;
import mini.xdab.singleton.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MessagesBuffer {

    private ByteArrayOutputStream mainBuffer;
    private ByteArrayOutputStream tempBuffer;

    public MessagesBuffer() {
        mainBuffer = new ByteArrayOutputStream();
        tempBuffer = new ByteArrayOutputStream();
    }

    public Integer size() {
        return mainBuffer.size() + tempBuffer.size();
    }

    public void beginMessage() {
        tempBuffer.reset();
    }

    public void addMessageByte(@NonNull Byte b) {
        tempBuffer.write(b);
    }

    public void abortMessage() {
        tempBuffer.reset();
    }

    public void concludeMessage() {
        if (tempBuffer.size() > 0) {
            try { mainBuffer.write(tempBuffer.toByteArray()); }
            catch (IOException ioe) {
                Log.error("MessagesBuffer.concludeMessage Unable to write from temp to main buffer: %s", ioe.getMessage());
            }
        }
        tempBuffer.reset();
    }

    public byte[] getMessages() {
        return mainBuffer.toByteArray();
    }

}
