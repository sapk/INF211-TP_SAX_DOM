package tpSAX;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxRSS {

    //-----------------------------------------------------------------------------
    private String url = "http://rss.lemonde.fr/c/205/f/3050/index.rss";
  // Mais aussi :
    //   http://rss.lemonde.fr/c/205/f/3050/index.rss
    //   http://java.developpez.com/index/rss
    //   http://www.francemusique.fr/rss.xml
    //   Etc.

    // Liste des mots clés à filtrer dans le flux d'information
    // Si la valeur du filtre vaut null ou est vide, le filtre ne s'applique pas.
    private String filtre = "lyon Pen Astérix Moscou";

    //-----------------------------------------------------------------------------
    public void performDemo() {
        if (filtre.equals("") || filtre == null) {
            return;
        }

        try {
            // Instanciation de l'analyseur XML (parser)
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            // On lance l'analyse du flux RSS disponible à l'URL fixée, la manière de
            // gérer ce flux est codée dans la classe RSSHandler.
            saxParser.parse(url, new RSSHandler());
        } catch (SAXException | ParserConfigurationException e) {
            System.err.println("Erreur SAX : " + e);
        } catch (IOException e) {
            System.err.println("Erreur d’E/S : " + e);
        }
    }

    //-----------------------------------------------------------------------------
    public static void main(String[] args) {
        System.out.println("C'est parti mon kiki !");
        new SaxRSS().performDemo();
    }

    //-----------------------------------------------------------------------------
    class RSSHandler extends DefaultHandler {
        // Drapeaux permettant de préciser le contexte de l'élément en cours
        // de traitement.

        private boolean isInsideItem = false;
        private boolean isItemMatching = false;
        private boolean isToAnalyze = false;

        private String currentElementType = null;
        private HashMap<String, String> itemData = null;

        // Compteur d'articles correspondant au filtre
        private int nbArticlesTrouves = 0;

        // +------------------------------------------------------------------+
        // | Méthodes de la super classe "DefaultHandler" à redéfinir dans la |
        // | classe "RSSHandler" :                                            |
        // |   - startDocument()                                              |
        // |   - endDocument()                                                |
        // |   - startElement(...)                                            |
        // |   - Etc.                                                         |
        // +------------------------------------------------------------------+
        // Note : pour redéfinir une méthode de la super classe avec Eclipse :
        // Clic droit dans le code > Source > Override/Implement Methods...
        // puis sélectionner la(les) méthodes à redéfinir.
        @Override
        public void startDocument() throws SAXException {
            System.out.println("=======  Début du document  =======");
        }

        @Override
        public void endDocument() throws SAXException {
            System.out.println("Nombre d'articles qui matchent : "+nbArticlesTrouves);
            System.out.println("=======   Fin du document   =======");

        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            //System.out.println("uri : " + uri + ", localName : " + localName + ", qName : " + qName + ", Attributes : " + attributes.toString());
            //TODO à filter que si on est dans les items
            if (qName == "item") {
                System.out.println("***      Début d'un item     ***");
                isInsideItem = true;
                itemData = new HashMap<>();
            } else if (isInsideItem) {
                currentElementType = qName;
                //On doit analyser le contenu si c'est un tilte ou une description
                isToAnalyze = (qName == "title" || qName == "description");
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            //System.out.println("uri : " + uri + ", localName : " + localName + ", qName : " + qName);
            if (qName == "item") {
                if (isItemMatching) {
                    System.out.println("   \\o/\\o/\\o/\\o/\\o/\\o/\\o/\\o/\\o/  ");
                    for (Map.Entry<String, String> data : itemData.entrySet()) {
                        String key = data.getKey();
                        String value = data.getValue();
                        System.out.println(key + " -> " + value);
                    }
                    nbArticlesTrouves++;
                    isItemMatching = false;
                }
                System.out.println("***       Fin d'un item      ***");
                isInsideItem = false;
            }
        }

        public void characters(char ch[], int start, int length) throws SAXException {

            if (isToAnalyze) {
                String value = new String(ch, start, length);
                itemData.put(currentElementType, value);
                //System.out.println(currentElementType + " -> Value : " + value);
                for (String aChercher : filtre.split(" ")) {
                    if (value.contains(aChercher)) {
                        isItemMatching = true;
                    }
                }
                isToAnalyze = false;
            }

        }
    }
  //-----------------------------------------------------------------------------
}
