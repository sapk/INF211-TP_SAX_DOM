package tpSAX;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxRSS
{
  //-----------------------------------------------------------------------------
  private String url = "http://rss.lemonde.fr/c/205/f/3050/index.rss";
  // Mais aussi :
  //   http://rss.lemonde.fr/c/205/f/3050/index.rss
  //   http://java.developpez.com/index/rss
  //   http://www.francemusique.fr/rss.xml
  //   Etc.
  
  // Liste des mots clés à filtrer ans le flux d'information
  // Si la valeur du filtre vaut null ou est vide, le filtre ne s'applique pas.
  private String filtre = "Terme_à_rechercher_Attention_sensible_à_la_casse";
  //-----------------------------------------------------------------------------
  public void performDemo()
  {
    try
    {
      // Instanciation de l'analyseur XML (parser)
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
      
      // On lance l'analyse du flux RSS disponible à l'URL fixée, la manière de
      // gérer ce flux est codée dans la classe RSSHandler.
      saxParser.parse(url, new RSSHandler());
    }
    catch (SAXException | ParserConfigurationException e)
    {
      System.err.println("Erreur SAX : " + e);
    }
    catch (IOException e)
    {
      System.err.println("Erreur d’E/S : " + e);
    }
  }
  //-----------------------------------------------------------------------------
  public static void main(String[] args)
  {
    System.out.println("C'est parti mon kiki !");
    new SaxRSS().performDemo();
  }
  //-----------------------------------------------------------------------------
  class RSSHandler extends DefaultHandler
  {
    // Drapeaux permettant de préciser le contexte de l'élément en cours
    // de traitement.
    private boolean isInsideItem        = false;
    
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

  }
  //-----------------------------------------------------------------------------
}
