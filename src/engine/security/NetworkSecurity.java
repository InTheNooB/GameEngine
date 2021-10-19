package engine.security;

import engine.GameContainer;
import static engine.security.EncryptionHandler.*;
import javax.xml.bind.DatatypeConverter;

public class NetworkSecurity {

    private NetworkSecurityType securityType;

    public NetworkSecurity(NetworkSecurityType securityType) {
        this.securityType = securityType;
    }

    public String encryptData(GameContainer gc, String data) {
        switch (securityType) {
            case SYMMETRIC_KEY:
            default:
                return symmetricEncryption(gc, data);
        }
    }

    public String decryptData(GameContainer gc, String data) {
        try {
            switch (securityType) {
                case SYMMETRIC_KEY:
                default:
                    return symmetricDecryption(gc, data);
            }
        } catch (Exception e) {
            gc.getEventHistory().addEvent("Error decrypting data : " + data);
            return null;
        }
    }

    public String decryptData(GameContainer gc, byte[] data) {
        switch (securityType) {
            case SYMMETRIC_KEY:
            default:
                return symmetricDecryption(gc, data);
        }
    }

    private String symmetricEncryption(GameContainer gc, String data) {
        byte[] cipherBytes = do_AESEncryption(gc, data);
        return DatatypeConverter.printHexBinary(cipherBytes);
    }

    private String symmetricDecryption(GameContainer gc, String data) {
        return do_AESDecryption(gc, data);
    }

    private String symmetricDecryption(GameContainer gc, byte[] data) {
        return do_AESDecryption(gc, data);
    }

    public NetworkSecurityType getSecurityType() {
        return securityType;
    }

    public void setSecurityType(NetworkSecurityType securityType) {
        this.securityType = securityType;
    }

    public static byte[] fromHexString(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

}
