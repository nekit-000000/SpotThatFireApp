package com.example.a810200.spotthatfireapp.Finder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FinderPlaces {
    private static final String apiUrl = "http://api.geonames.org/search?maxRows=4&";
    private static final String endOfUrl = "&username=treeonfire";

    private static final String getStartWith = "name_startsWith=";
    private static final String getEqualsWith = "name_equals=";

    private ArrayList<GeomObject> LoadAndParse(String url) {
        Document res = null;
        ArrayList<GeomObject> gobjects = new ArrayList<>();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            res = db.parse(url);
        }catch(ParserConfigurationException pce) {
            pce.printStackTrace();
            return  null;
        }catch(SAXException se) {
            se.printStackTrace();
            return null;
        }catch(IOException ioe) {
            ioe.printStackTrace();
            return null;
        }

        Element docEle = res.getDocumentElement();

        NodeList nl = docEle.getElementsByTagName("geoname");
        if (nl != null && nl.getLength() > 0) {
            for(int i = 0 ; i < nl.getLength(); i++) {
                Element el = (Element)nl.item(i);
                GeomObject go = getGeomObj(el);
                gobjects.add(go);
            }
        }

        return gobjects;
    }

    private GeomObject getGeomObj(Element geomObjEl) {
        //for each <employee> element get text or int values of
        //name ,id, age and name
        String name = getTextValue(geomObjEl,"name");
        double lat = getDoubleValue(geomObjEl,"lat");
        double lng = getDoubleValue(geomObjEl,"lng");
        String countryName = getTextValue(geomObjEl,"countryName");

        //Create a new Employee with the value read from the xml nodes
        GeomObject go = new GeomObject(name, countryName, lat, lng);
        return go;
    }

    private String getTextValue(Element ele, String tagName) {
        String textVal = null;
        NodeList nl = ele.getElementsByTagName(tagName);
        if(nl != null && nl.getLength() > 0) {
            Element el = (Element)nl.item(0);
            textVal = el.getFirstChild().getNodeValue();
        }
        return textVal;
    }

    private double getDoubleValue(Element ele, String tagName) {
        return Double.parseDouble(getTextValue(ele, tagName));
    }

    public List<GeomObject> GetStartWith(String beginString) {
        String finalURL = apiUrl + getStartWith + beginString + endOfUrl;
        return LoadAndParse(finalURL);
    }

    public GeomObject EqualsWith(String string) {
        String finalURL = apiUrl + getEqualsWith + string + endOfUrl;
        return LoadAndParse(finalURL).get(0);
    }
}
