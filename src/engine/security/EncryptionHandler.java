package engine.security;

import engine.GameContainer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public abstract class EncryptionHandler {

    public static final String SYMMETRIC_KEY = "la8Vj5tiXGjFcaDcS0bwDLzJEdNgMf6uYOC3L7O4hTQ=";
    public static final String SYMMETRIC_VECTOR = "0A27DA59872DEB4F27439157C471721C";

    private static final String AES = "AES";

    private static final String AES_CIPHER_ALGORITHM = "AES/CBC/PKCS5PADDING";

    private static SecretKey createAESKey(GameContainer gc) {
        try {
            SecureRandom securerandom = new SecureRandom();
            KeyGenerator keygenerator = KeyGenerator.getInstance(AES);

            keygenerator.init(256, securerandom);
            SecretKey key = keygenerator.generateKey();

            return key;
        } catch (NoSuchAlgorithmException ex) {
            gc.getEventHistory().addEvent("NoSuchAlgorithmException : EncryptionHandler.createAESKey()");
            return null;
        }
    }

    private static byte[] createInitializationVector() {
        byte[] initializationVector = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(initializationVector);
        return initializationVector;
    }

    public static byte[] do_AESEncryption(GameContainer gc, String plainText) {
        try {
            SecretKey secretKey = stringToSecreyKey(SYMMETRIC_KEY);
            Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);

            IvParameterSpec ivParameterSpec = new IvParameterSpec(DatatypeConverter.parseHexBinary(SYMMETRIC_VECTOR));

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

            return cipher.doFinal(plainText.getBytes());
        } catch (NoSuchAlgorithmException ex) {
            gc.getEventHistory().addEvent("NoSuchAlgorithmException : EncryptionHandler.do_AESEncryption()");
        } catch (NoSuchPaddingException ex) {
            gc.getEventHistory().addEvent("NoSuchPaddingException : EncryptionHandler.do_AESEncryption()");
        } catch (InvalidKeyException ex) {
            gc.getEventHistory().addEvent("InvalidKeyException : EncryptionHandler.do_AESEncryption()");
        } catch (InvalidAlgorithmParameterException ex) {
            gc.getEventHistory().addEvent("InvalidAlgorithmParameterException : EncryptionHandler.do_AESEncryption()");
        } catch (IllegalBlockSizeException ex) {
            gc.getEventHistory().addEvent("IllegalBlockSizeException : EncryptionHandler.do_AESEncryption()");
        } catch (BadPaddingException ex) {
            gc.getEventHistory().addEvent("BadPaddingException : EncryptionHandler.do_AESEncryption()");
        }
        return null;
    }

    public static String do_AESDecryption(GameContainer gc, String cipherText) {
        try {
            SecretKey secretKey = stringToSecreyKey(SYMMETRIC_KEY);
            Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);

            IvParameterSpec ivParameterSpec = new IvParameterSpec(DatatypeConverter.parseHexBinary(SYMMETRIC_VECTOR));

            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            byte[] result = cipher.doFinal(DatatypeConverter.parseHexBinary(cipherText));

            return new String(result);
        } catch (NoSuchAlgorithmException ex) {
            gc.getEventHistory().addEvent("NoSuchAlgorithmException : EncryptionHandler.do_AESDecryption()");
        } catch (NoSuchPaddingException ex) {
            gc.getEventHistory().addEvent("NoSuchPaddingException : EncryptionHandler.do_AESDecryption()");
        } catch (InvalidKeyException ex) {
            gc.getEventHistory().addEvent("InvalidKeyException : EncryptionHandler.do_AESDecryption()");
        } catch (InvalidAlgorithmParameterException ex) {
            gc.getEventHistory().addEvent("InvalidAlgorithmParameterException : EncryptionHandler.do_AESDecryption()");
        } catch (IllegalBlockSizeException ex) {
            gc.getEventHistory().addEvent("IllegalBlockSizeException : EncryptionHandler.do_AESDecryption()");
        } catch (BadPaddingException ex) {
            gc.getEventHistory().addEvent("BadPaddingException : EncryptionHandler.do_AESDecryption()");
        }
        return null;
    }

    public static String do_AESDecryption(GameContainer gc, byte[] bytes) {
        try {
            SecretKey secretKey = stringToSecreyKey(SYMMETRIC_KEY);
            Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);

            IvParameterSpec ivParameterSpec = new IvParameterSpec(DatatypeConverter.parseHexBinary(SYMMETRIC_VECTOR));

            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            byte[] result = cipher.doFinal(bytes);

            return new String(result);
        } catch (NoSuchAlgorithmException ex) {
            gc.getEventHistory().addEvent("NoSuchAlgorithmException : EncryptionHandler.do_AESDecryption()");
        } catch (NoSuchPaddingException ex) {
            gc.getEventHistory().addEvent("NoSuchPaddingException : EncryptionHandler.do_AESDecryption()");
        } catch (InvalidKeyException ex) {
            gc.getEventHistory().addEvent("InvalidKeyException : EncryptionHandler.do_AESDecryption()");
        } catch (InvalidAlgorithmParameterException ex) {
            gc.getEventHistory().addEvent("InvalidAlgorithmParameterException : EncryptionHandler.do_AESDecryption()");
        } catch (IllegalBlockSizeException ex) {
            ex.printStackTrace();
            gc.getEventHistory().addEvent("IllegalBlockSizeException : EncryptionHandler.do_AESDecryption()");
        } catch (BadPaddingException ex) {
            gc.getEventHistory().addEvent("BadPaddingException : EncryptionHandler.do_AESDecryption()");
        }
        return null;
    }

    private static SecretKey stringToSecreyKey(String s) {
        byte[] decodedKey = Base64.getDecoder().decode(s);
        SecretKey k = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        return k;
    }
}
