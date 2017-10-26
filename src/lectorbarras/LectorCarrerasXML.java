/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lectorbarras;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author ricardo
 */
public class LectorCarrerasXML {

    private static final String url = "./src/resources/Carreras.xml";
    private LinkedList<Carrera> carreras;
    private Document doc;

    public LectorCarrerasXML() {
        this.carreras = new LinkedList<Carrera>();
        try {
            File inputFile = new File(url);
            SAXBuilder saxBuilder = new SAXBuilder();
            this.doc = saxBuilder.build(inputFile);
            Element classElement = this.doc.getRootElement();
            List<Element> carrerasList = classElement.getChildren();
            for (int temp = 0; temp < carrerasList.size(); temp++) {
                Element carrera = carrerasList.get(temp);
                Attribute attribute = carrera.getAttribute("id");
                String idString = attribute.getValue();
                int id = Integer.parseInt(idString);
                String nombre = carrera.getChild("nombre").getText();
                this.carreras.add(new Carrera(id, nombre));
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public LinkedList<Carrera> getCarreras() {
        return this.carreras;
    }

    public List<String> getNombres() {
        List<String> regreso = new LinkedList<String>();
        for(Carrera c : this.carreras) {
            regreso.add(c.getNombre());
        }
        return regreso;
    }
    
    public void addCarrera(Carrera carrera) throws FileNotFoundException, IOException {
        Element element = new Element("carrera");
        element.setAttribute("id", Integer.toString(carrera.getId()));
        Element nombre = new Element("nombre");
        nombre.setText(carrera.getNombre());
        element.addContent(nombre);
        this.doc.getRootElement().addContent(element);
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        xmlOutputter.output(this.doc, new FileOutputStream(url));
    }

    public void deleteCarrera(Carrera carrera) throws Exception {
        String idString = Integer.toString(carrera.getId());
        Element classElement = this.doc.getRootElement();
        List<Element> carrerasList = classElement.getChildren();
        for (int temp = 0; temp < carrerasList.size(); temp++) {
            Element elem = carrerasList.get(temp);
            Attribute attribute = elem.getAttribute("id");
            if(attribute.getValue().equals(idString)) {
                elem.detach();
                break;
            }
        }        
         XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
         xmlOutputter.output(this.doc, new FileOutputStream(url));
    }

   public Carrera getCarreras(String cadena) {
       for(int i = 0; i < this.carreras.size(); i++) {
           if(this.carreras.get(i).getNombre().equalsIgnoreCase(cadena)) {
               return this.carreras.get(i);
           }
       }
       return null;
    }
}
