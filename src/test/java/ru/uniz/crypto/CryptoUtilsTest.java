package ru.uniz.crypto;

import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessServiceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CryptoUtilsTest {

    @Test
    void Crypt() throws SiebelBusinessServiceException {
        CryptoUtils cryptoUtils = new CryptoUtils();
        SiebelPropertySet in = new SiebelPropertySet();
        SiebelPropertySet out = new SiebelPropertySet();

        in.setProperty("Value" ,"ПИРОЖОЧЕК");
        cryptoUtils.doInvokeMethod("HASH512", in, out);
        assertTrue(out.getProperty("Value").contains("430c4ca5079e17427064e479a4a4ab4e3981b2e07e41471a414381852026adf37271b1f8c6ac2648d18fbffb7c037f2c99961cd764802b75f4c004de57713f14"));
    }
}