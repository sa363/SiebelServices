package ru.uniz.xml;

import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessServiceException;
import org.junit.jupiter.api.Test;
import ru.uniz.xml.XMLUtils;

import static org.junit.jupiter.api.Assertions.*;

class XMLUtilsTest {

    @Test
    void XML2JSON() throws SiebelBusinessServiceException {
        XMLUtils xmlUtils = new XMLUtils();
        SiebelPropertySet in = new SiebelPropertySet();
        SiebelPropertySet out = new SiebelPropertySet();
        String xml = data.data;
        in.setProperty("data", xml);
        xmlUtils.doInvokeMethod("XML2JSON", in, out);
        System.out.println(out.getValue());
        assertTrue(out.getValue().contains("{"));
    }

    @Test
    void formatXML() throws SiebelBusinessServiceException {
        XMLUtils xmlUtils = new XMLUtils();
        SiebelPropertySet in = new SiebelPropertySet();
        SiebelPropertySet out = new SiebelPropertySet();
        String xml = data.data;
        in.setProperty("xml", xml);
        in.setProperty("ident", "6");
        xmlUtils.doInvokeMethod("FORMATXML", in, out);
        System.out.println(out.getValue());
        assertFalse(out.getValue().isEmpty());
    }
}