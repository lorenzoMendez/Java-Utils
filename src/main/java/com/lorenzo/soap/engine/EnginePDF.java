package com.lorenzo.soap.engine;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class EnginePDF {
	
	//private String path_;
	private String styles;
	private String[] pages;
	private Map<String, String> fieldMaps;
	private Map<String, String> fontMaps;
	private static final Logger logger = LogManager.getLogger( EnginePDF.class );
	
	public void /*byte[]*/ builderDocument( String file, String path ) {
		final String head = "<html><head><meta charset='UTF-8' /><style>" + this.styles.toString() + "</style></head><body>";
        final String tail = "</body></html>";
        
	}
	
	@SuppressWarnings("unused")
	private void loadData( String path, String fileName ) throws Exception {
		
		LinkedList<String> itemsList = new LinkedList<String>();
		int total = 0;
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		FileInputStream file = new FileInputStream( this.getClass().getResource( path + fileName ).getPath() );
		
		// Parse document as XML Object
		Document xmlDocument = docBuilder.parse( file );
		xmlDocument.getDocumentElement().normalize();
		
		// Get the 
		NodeList node1 = xmlDocument.getElementsByTagName( "path" );
		if( node1.getLength() <= 0 )
			throw new Exception( "Error, not path." );
		
		NodeList node2 = xmlDocument.getElementsByTagName( "pages" );
		if( node2.getLength() <= 0 )
			throw new Exception( "Error, not documents." );
		
		Element element = (Element) node2.item( 0 );
		NodeList childNodes = element.getChildNodes();
		
		for( int i = 0; i < childNodes.getLength(); ++i, total++ ) {
			if( childNodes.item( i ).getNodeType() == 1 ) {
				Element tmp = (Element) childNodes.item( i );
				itemsList.add( tmp.getTextContent().trim() );
			}
		}
		this.pages = new String[ total ];
		
		for( int i = 0; i < total; ++i ) {
			this.pages[ i ] = itemsList.get( i );
		}
		
		NodeList nodeStyles = xmlDocument.getElementsByTagName( "styles" );
		
		if( nodeStyles.getLength() > 0 ) {
			Element elementStyle = (Element) nodeStyles.item( 0 );
			this.styles = elementStyle.getTextContent().trim();
			
			NodeList fields = xmlDocument.getElementsByTagName( "fields" );
			
			if( fields.getLength() > 0 ) {
				Element field = (Element)fields.item(0);
                NodeList items = field.getChildNodes();
                for( int i = 0; i < items.getLength(); ++i ) {
                	if( items.item( i ).getNodeType() == 1 ) {
                		Element el3 = (Element)items.item(i);
                        this.fieldMaps.put( el3.getAttribute("name").trim(), el3.getAttribute("type").trim() );
                	}
                }
			}
			
			NodeList fonts = xmlDocument.getElementsByTagName("fonts");
			fontMaps = new HashMap<>();
			if( fonts.getLength() > 0 ) {
				final Element font = (Element)fonts.item(0);
                final NodeList items = font.getChildNodes();
                for (int j = 0; j < items.getLength(); ++j) {
                    if( items.item(j).getNodeType() == 1 ) {
                        final Element el7 = (Element)items.item(j);
                        this.fontMaps.put( el7.getTextContent().trim(), el7.getTextContent().trim());
                    }
                }
			}
			return;
		}
		throw new Exception( "Error al cargar el document " + fileName );
	}
}