import org.xml.sax.helpers.DefaultHandler;


public class SAXParserMain extends DefaultHandler {



    public static void main(String[] args) {
        SAXParserExperiment spe = new SAXParserExperiment();
        spe.runExample();

        SAXParserActors spa = new SAXParserActors();
        spa.runExample();

        SAXParserCast spc = new SAXParserCast();
        spc.runExample();

    }

}
