package engine.io;

import engine.GameContainer;
import engine.consts.FileConstants;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLReader implements FileConstants {

    /**
     *
     * @param gc
     * @param filePath The file path (Not including the first main folder (Ex.
     * "map/file.xml")
     * @param search The xPath search to get multiple nodes
     * @param node The searched value (Ex. "name", "coordX")
     * @param nodeNb The index inside the multiple nodes
     * @return
     */
    public static String getStringValue(GameContainer gc, String filePath, String search, String node, int nodeNb) {
        String value = null;
        try {
            File file = new File(FOLDER_ASSETS + SPR + filePath);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder;
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            XPathFactory xpf = XPathFactory.newInstance();
            XPath path = xpf.newXPath();

            String expression = search;
            NodeList nodes = (NodeList) path.evaluate(expression, document, XPathConstants.NODESET);

            Node n = nodes.item(nodeNb);

            path.compile(node);
            value = (String) path.evaluate(node, n, XPathConstants.STRING);
        } catch (XPathExpressionException | ParserConfigurationException | SAXException | IOException ex) {
            gc.getEventHistory().addEvent("Error On XML load/getValue : " + filePath + " -> " + node);
        }
        return value;
    }

    /**
     *
     * @param gc
     * @param filePath The file path (Not including the first main folder (Ex.
     * "map/file.xml")
     * @param node The node containing the value (Ex. "/map/name")
     * @return
     */
    public static String getStringValue(GameContainer gc, String filePath, String node) {
        String value = null;
        try {
            File file = new File(FOLDER_ASSETS + SPR + filePath);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder;
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            XPathFactory xpf = XPathFactory.newInstance();
            XPath path = xpf.newXPath();

            value = (String) path.evaluate(node, document, XPathConstants.STRING);
        } catch (XPathExpressionException | ParserConfigurationException | SAXException | IOException ex) {
            gc.getEventHistory().addEvent("Error On XML load/getValue : " + filePath + " -> " + node);
        }
        return value;
    }

    /**
     *
     * @param gc
     * @param filePath The file path (Not including the first main folder (Ex.
     * "map/file.xml")
     * @param search The xPath search to get multiple nodes
     * @param node The searched value (Ex. "name", "coordX")
     * @param nodeNb The index inside the multiple nodes
     * @return
     */
    public static int getIntValue(GameContainer gc, String filePath, String search, String node, int nodeNb) {
        int value = -1;
        try {
            File file = new File(FOLDER_ASSETS + SPR + filePath);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder;
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            XPathFactory xpf = XPathFactory.newInstance();
            XPath path = xpf.newXPath();

            String expression = search;
            NodeList nodes = (NodeList) path.evaluate(expression, document, XPathConstants.NODESET);

            Node n = nodes.item(nodeNb);

            path.compile(node);
            value = ((Double) path.evaluate(node, n, XPathConstants.NUMBER)).intValue();
        } catch (XPathExpressionException | ParserConfigurationException | SAXException | IOException ex) {
            gc.getEventHistory().addEvent("Error On XML load/getValue : " + filePath + " -> " + node);
        }
        return value;
    }

    /**
     *
     * @param gc
     * @param filePath The file path (Not including the first main folder (Ex.
     * "map/file.xml")
     * @param node The node containing the value (Ex. "/map/name")
     * @return
     */
    public static int getIntValue(GameContainer gc, String filePath, String node) {
        int value = -1;
        try {
            File file = new File(FOLDER_ASSETS + SPR + filePath);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder;
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            XPathFactory xpf = XPathFactory.newInstance();
            XPath path = xpf.newXPath();

            value = ((Double) path.evaluate(node, document, XPathConstants.NUMBER)).intValue();
        } catch (XPathExpressionException | ParserConfigurationException | SAXException | IOException ex) {
            gc.getEventHistory().addEvent("Error On XML load/getValue : " + filePath + " -> " + node);
        }
        return value;
    }

    /**
     *
     * @param gc
     * @param filePath The file path (Not including the first main folder (Ex.
     * "map/file.xml")
     * @param search The xPath search to get multiple nodes
     * @param node The searched value (Ex. "name", "coordX")
     * @param nodeNb The index inside the multiple nodes
     * @return
     */
    public static float getFloatValue(GameContainer gc, String filePath, String search, String node, int nodeNb) {
        float value = -1f;
        try {
            File file = new File(FOLDER_ASSETS + SPR + filePath);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder;
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            XPathFactory xpf = XPathFactory.newInstance();
            XPath path = xpf.newXPath();

            String expression = search;
            NodeList nodes = (NodeList) path.evaluate(expression, document, XPathConstants.NODESET);

            Node n = nodes.item(nodeNb);

            path.compile(node);
            value = ((Double) path.evaluate(node, n, XPathConstants.NUMBER)).floatValue();
        } catch (XPathExpressionException | ParserConfigurationException | SAXException | IOException ex) {
            gc.getEventHistory().addEvent("Error On XML load/getValue : " + filePath + " -> " + node);
        }
        return value;
    }

    /**
     *
     * @param gc
     * @param filePath The file path (Not including the first main folder (Ex.
     * "map/file.xml")
     * @param node The node containing the value (Ex. "/map/name")
     * @return
     */
    public static float getFloatValue(GameContainer gc, String filePath, String node) {
        float value = -1f;
        try {
            File file = new File(FOLDER_ASSETS + SPR + filePath);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder;
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            XPathFactory xpf = XPathFactory.newInstance();
            XPath path = xpf.newXPath();

            value = ((Double) path.evaluate(node, document, XPathConstants.NUMBER)).floatValue();
        } catch (XPathExpressionException | ParserConfigurationException | SAXException | IOException ex) {
            gc.getEventHistory().addEvent("Error On XML load/getValue : " + filePath + " -> " + node);
        }
        return value;
    }
    /**
     *
     * @param gc
     * @param filePath The file path (Not including the first main folder (Ex.
     * "map/file.xml")
     * @param search The xPath search to get multiple nodes
     * @param node The searched value (Ex. "name", "coordX")
     * @param nodeNb The index inside the multiple nodes
     * @return
     */
    public static boolean getBooleanValue(GameContainer gc, String filePath, String search, String node, int nodeNb) {
        boolean value = false;
        try {
            File file = new File(FOLDER_ASSETS + SPR + filePath);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder;
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            XPathFactory xpf = XPathFactory.newInstance();
            XPath path = xpf.newXPath();

            String expression = search;
            NodeList nodes = (NodeList) path.evaluate(expression, document, XPathConstants.NODESET);

            Node n = nodes.item(nodeNb);

            path.compile(node);
            value = path.evaluate(node, n, XPathConstants.STRING).equals("true");
        } catch (XPathExpressionException | ParserConfigurationException | SAXException | IOException ex) {
            gc.getEventHistory().addEvent("Error On XML load/getValue : " + filePath + " -> " + node);
        }
        return value;
    }

    /**
     *
     * @param gc
     * @param filePath The file path (Not including the first main folder (Ex.
     * "map/file.xml")
     * @param node The node containing the value (Ex. "/map/name")
     * @return
     */
    public static boolean getBooleanValue(GameContainer gc, String filePath, String node) {
        boolean value = false;
        try {
            File file = new File(FOLDER_ASSETS + SPR + filePath);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder;
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            XPathFactory xpf = XPathFactory.newInstance();
            XPath path = xpf.newXPath();

            value = path.evaluate(node, document, XPathConstants.STRING).equals("true");
        } catch (XPathExpressionException | ParserConfigurationException | SAXException | IOException ex) {
            gc.getEventHistory().addEvent("Error On XML load/getValue : " + filePath + " -> " + node);
        }
        return value;
    }

}
