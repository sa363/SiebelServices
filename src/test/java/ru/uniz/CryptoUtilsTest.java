package ru.uniz;

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

        in.setProperty("Value" ,"Секретное слово");
        cryptoUtils.doInvokeMethod("CRYPT", in, out);
        assertTrue(out.getProperty("Value").contains("0c1c655ff04d3d29a318867e3d9d2393c2d8dfaebfed405146ac70a0f1163d7a"));
    }
}