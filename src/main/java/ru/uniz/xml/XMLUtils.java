package ru.uniz.xml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessService;
import com.siebel.eai.SiebelBusinessServiceException;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

public class XMLUtils extends SiebelBusinessService {
    private enum Methods {
        XML2JSON, FORMATXML
    }
    @Override
    public void doInvokeMethod(String methodName, SiebelPropertySet siebelPSIn, SiebelPropertySet siebelPSOut) throws SiebelBusinessServiceException {
        try {
            switch (Methods.valueOf(methodName.toUpperCase())) {
                case XML2JSON:
                    xml2json(siebelPSIn, siebelPSOut);
                    break;
                case FORMATXML:
                    formatXML(siebelPSIn, siebelPSOut);
                    break;
            }
        }catch (Exception e) {
            if(e.getClass().isInstance(SiebelBusinessServiceException.class)) {
                throw (SiebelBusinessServiceException) e;
            }
            throw new SiebelBusinessServiceException(e, "SBL-UNZ-ERR", e.getMessage());
        }
    }

    private void formatXML(SiebelPropertySet siebelPSIn, SiebelPropertySet siebelPSOut) throws SiebelBusinessServiceException {
        if (siebelPSIn == null && siebelPSIn.getChild(0) == null) {
            throw new SiebelBusinessServiceException("Error", "Property xml is not null");
        }
        String xml = siebelPSIn.getProperty("xml");
        int ident = (siebelPSIn.getProperty("ident") != null) ? Integer.parseInt(siebelPSIn.getProperty("ident")) : 6;
        Document document = convertStringToDocument(xml);
        siebelPSOut.setValue(toPrettyXmlString(ident, document));
    }

    private void xml2json(SiebelPropertySet siebelPSIn, SiebelPropertySet siebelPSOut) throws IOException, SiebelBusinessServiceException {
        if (siebelPSIn == null && siebelPSIn.getChild(0) == null) {
            throw new SiebelBusinessServiceException("Error", "Property data is not null");
        }
            String xml = siebelPSIn.getProperty("data");
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode jsonNode = xmlMapper.readTree(xml.getBytes("UTF-8"));
            ObjectMapper objectMapper = new ObjectMapper();
            String value = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
            siebelPSOut.setValue(value);
    }

    private Document convertStringToDocument(String xml) {
        try {
            return DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(new InputSource(new ByteArrayInputStream(xml.getBytes("UTF-8"))));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    // в переменной indent указываем уровень(величину) отступа
    private String toPrettyXmlString(int indent, Document document) {
        try {
            // удаляем пробелы
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.evaluate(
                    "//text()[normalize-space()='']",
                    document,
                    XPathConstants.NODESET
            );

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                node.getParentNode().removeChild(node);
            }

            // устанавливаем настройки для красивого форматирования
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", indent);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // форматируем XML
            StringWriter stringWriter = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(stringWriter));

            // возвращаем строку с отформатированным XML
            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
