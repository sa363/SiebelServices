package ru.uniz;

import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessService;
import com.siebel.eai.SiebelBusinessServiceException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoUtils extends SiebelBusinessService {
    private enum Methods {
        CRYPT, DECRYPT
    }


    @Override
    public void doInvokeMethod(String methodName,
                               SiebelPropertySet inputPS,
                               SiebelPropertySet outputPS) throws SiebelBusinessServiceException {

        try {
            switch (Methods.valueOf(methodName.toUpperCase())) {
                case CRYPT:
                    MessageCrypt(inputPS, outputPS);
                    break;
                case DECRYPT:
                    break;
            }
        } catch (IllegalArgumentException | NoSuchAlgorithmException e) {
            if(e.getClass().isInstance(SiebelBusinessServiceException.class)) {
                throw (SiebelBusinessServiceException) e;
            }
            throw new SiebelBusinessServiceException(e, "SBL-UNZ-ERR", e.getMessage());
        }

    }

    private void MessageCrypt(SiebelPropertySet inputPS, SiebelPropertySet outputPS) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(inputPS.getProperty("Value").getBytes(StandardCharsets.UTF_8));
        outputPS.setProperty("Value", bytesToHex(hash));
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
