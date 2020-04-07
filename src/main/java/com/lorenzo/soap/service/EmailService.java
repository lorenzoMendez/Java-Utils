package com.lorenzo.soap.service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.lorenzo.soap.commons.ConfigurationLoader;
import com.lorenzo.soap.commons.PaymentDescriptor;
import com.lorenzo.soap.util.NumberUtil;

@Service
public class EmailService {
	
	@Autowired
	private Properties initProperties;
	
	public List<String> getProperties() {
		List<String> listProperties = new ArrayList<>();
		
		listProperties.add( initProperties.getProperty( "KEY_FIRST" ) );
		listProperties.add( initProperties.getProperty( "KEY_SECOND" ) );
		
		// The Singleton ConfigurationLoader is load in this moment
		ConfigurationLoader loader = ConfigurationLoader.getInstance();
		
		System.out.println( loader.getValue1() );
		System.out.println( loader.getValue2() );
		
		return listProperties;
	}
	
	public void sendVoucherTest() throws Exception {
		PaymentDescriptor voucher = new PaymentDescriptor();
		String email = "lor90td@gmail.com";
		
		voucher.setAfiliation( "Texto afiliacion" );
		voucher.setAuthorization( "03498656" );
		voucher.setDirection( "Insurgentes Sur 5536, piso 99, Col. del Valle, Del. Benito Juárez, C.P. 03000, México, D.F." );
		voucher.setIssuer( "BBVA SANTANDER ETC" );
		voucher.setTransactionDate( "04-FEB-2020" );
		voucher.setTransactionHour( "09:10" );
		voucher.setAmount( new BigDecimal( "349.34" ) );
		voucher.setCardNumber( " 123**********678" );
		voucher.setOperationType( "234" );
		voucher.setPaymentType( "TDX" );
		voucher.setOwnerCard( "JAVIER HERNANDEZ GONZALEZ" );
		voucher.setReference( "3346" );
		voucher.setTotalAmount( new BigDecimal( "350.53" ) );
		
		byte[] pdf = this.buildVoucher( voucher );
		
		final String message = "This message has been send to you for testing my application."
				+ "<br><br>Feel free to response me, my email is lor90td@gmail.com</b><br>";
		
		MailThread mail = new MailThread( "Mail header", email, message, pdf );
		
		new Thread( mail ).start();
	}
	
	@SuppressWarnings("static-access")
	public byte[] buildVoucher( PaymentDescriptor voucher ) throws Exception {
    	
		String nm = String.valueOf( voucher.getAmount() );
    	String[] numero = nm.split( "\\." );
    	
		Paragraph paragraph = null;
		
    	float left = 30;
        float right = 30;
        float top = 35;
        float bottom = 10;
        
        try {
        	NumberUtil numberUtil = new NumberUtil();
        	
        	ByteArrayOutputStream stream = new ByteArrayOutputStream();
        	
        	Rectangle pageSize = new Rectangle( 420.0f, 520.0f );
        	
        	Document document = new Document( pageSize, left, right, top, bottom );
        	
        	String monto = numberUtil.convertirLetras( Integer.parseInt( numero[ 0 ] ) );
        	
        	Font fontNormal = FontFactory.getFont( "arial", 10, Font.NORMAL, BaseColor.BLACK );
        	Font fontGray = FontFactory.getFont( "arial", 10, Font.NORMAL, BaseColor.GRAY );
        	
        	PdfWriter.getInstance(document, stream);
        	
        	document.open();

        	document.setMargins( left, right, top, bottom );
        	
        	Chunk element = new Chunk();
        	Chunk atribute = new Chunk();
        	Phrase phrase = new Phrase();
        	
        	// Header elements
        	document.add( new Paragraph( "MY BUSINESS NAME", FontFactory.getFont( "arial", 12, Font.BOLD, BaseColor.BLACK ) ) );
        	document.add( new Paragraph( "SOMETHING", fontNormal ) );
        	document.add( new Paragraph( voucher.getOwnerCard(), fontGray ) );
        	document.add( new LineSeparator( 0.5f, 100, BaseColor.GRAY, 0, -5 ) );
        	document.add( new Paragraph( "\n", fontGray ) );
        	Paragraph comprobante = new Paragraph( "SOME MESSAGE", fontNormal );
        	comprobante.setAlignment( Element.ALIGN_CENTER );
        	document.add( comprobante );
        	
        	// Easy way to get the file in the resources items
        	Image img = Image.getInstance( this.getClass().getResource( "/img/logos/google_logo.jpg" ).getPath() );
        	
        	img.scaleAbsolute( 110f, 30f  );
        	img.setAbsolutePosition(280, 450);
        	document.add(img);
        	
        	PdfPTable table = new PdfPTable( 2 );
        	table.setTotalWidth( new float[] { 200, 160 } );
        	table.setLockedWidth( true );
        	
        	PdfPCell cell1 = new PdfPCell();
        	
            phrase = new Paragraph();
            element = new Chunk( "Razón social: ", fontNormal );
            atribute = new Chunk( "My Company Name", fontGray );
            phrase.add( element );
            phrase.add( atribute );
            cell1.addElement( phrase  );
            
            phrase = new Paragraph();            
            element = new Chunk( "Comercio: ", fontNormal );
            atribute = new Chunk( "Some title", fontGray );
            phrase.add( element );
            phrase.add( atribute );
            cell1.addElement( phrase  );
            
            phrase = new Paragraph();            
            element = new Chunk( "Afiliación: ", fontNormal );
            atribute = new Chunk( voucher.getAfiliation(), fontGray );
            phrase.add( element );
            phrase.add( atribute );
            cell1.addElement( phrase  );
            
            phrase = new Paragraph();            
            element = new Chunk( "Fecha: ", fontNormal );
            atribute = new Chunk( voucher.getTransactionDate(), fontGray );
            phrase.add( element );
            phrase.add( atribute );
            cell1.addElement( phrase  );
            
            phrase = new Paragraph();            
            element = new Chunk( "Hora: ", fontNormal );
            atribute = new Chunk( voucher.getTransactionHour(), fontGray );
            phrase.add( element );
            phrase.add( atribute );
            cell1.addElement( phrase  );
            
            phrase = new Paragraph();            
            element = new Chunk( "Emisor: ", fontNormal );
            atribute = new Chunk( voucher.getIssuer(), fontGray );
            phrase.add( element );
            phrase.add( atribute );
            cell1.addElement( phrase  );
        	
            phrase = new Paragraph();            
            element = new Chunk( "Folio: ", fontNormal );
            atribute = new Chunk( voucher.getReference(), fontGray );
            phrase.add( element );
            phrase.add( atribute );
            cell1.addElement( phrase  );
            
            phrase = new Paragraph();            
            element = new Chunk( "Tipo de Transacción: ", fontNormal );
            atribute = new Chunk( voucher.getOperationType(), fontGray );
            phrase.add( element );
            phrase.add( atribute );
            cell1.addElement( phrase  );
            
            String mood = voucher.getPaymentType();
            phrase = new Paragraph();            
            element = new Chunk( "Modo Ingreso: ", fontNormal );
            atribute = new Chunk( mood.equals( "I@1" ) ? "Insertada en línea" : mood.equals( "D@1" ) ? "Deslizada en línea" : "Digitada en línea", fontGray );
            phrase.add( element );
            phrase.add( atribute );
            cell1.addElement( phrase  );
        	
            cell1.setFixedHeight( 160 );
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell1.setBorder(Rectangle.NO_BORDER);
            cell1.setVerticalAlignment( cell1.ALIGN_TOP );
            table.addCell( cell1 );
            
            PdfPCell cell2 = new PdfPCell();
            
            phrase = new Paragraph();            
            element = new Chunk( "Número de Tarjeta: ", fontNormal );
            atribute = new Chunk( voucher.getCardNumber(), fontGray );
            phrase.add( element );
            phrase.add( atribute );
            cell2.addElement( phrase  );
            
            phrase = new Paragraph();            
            element = new Chunk( "Aprobación: ", fontNormal );
            atribute = new Chunk( voucher.getAuthorization(), fontGray );
            phrase.add( element );
            phrase.add( atribute );
            cell2.addElement( phrase  );
            
            phrase = new Paragraph();            
            element = new Chunk( "Monto: ", fontNormal );
            atribute = new Chunk( "$" + nm + " MXN", fontGray );
            phrase.add( element );
            phrase.add( atribute );
            cell2.addElement( phrase  );
            
            phrase = new Paragraph();            
            element = new Chunk( "Monto en letras: ", fontNormal );
            atribute = new Chunk( monto + " PESOS " + numero[ 1 ] +"/100 MXP", fontGray );
            phrase.add( element );
            phrase.add( atribute );
            cell2.addElement( phrase  );
            
            cell2.setBorder(Rectangle.NO_BORDER);
            table.addCell( cell2 );
            
            document.add( table );
            
            Paragraph phrase1 = new Paragraph();
            Chunk atribute1 = new Chunk( voucher.getDirection() + "\n", FontFactory.getFont( "arial", 8, Font.NORMAL, BaseColor.GRAY ) );
            phrase1.add( atribute1 );
            phrase1.setAlignment( Element.ALIGN_CENTER );
            document.add( phrase1 );
            
            Paragraph phrase2 = new Paragraph();
            Chunk atribute2 = new Chunk( "Para dudas o aclaraciones contacte a nuestra línea de atención a clientes 4095039450 ó mynombre@correo.com.mx\n\n", FontFactory.getFont( "arial", 8, Font.NORMAL, BaseColor.GRAY ) );
            phrase2.add( atribute2 );
            phrase2.setAlignment( Element.ALIGN_CENTER );
            document.add( phrase2 );
            
        	document.add(new LineSeparator( 0.5f, 100, BaseColor.GRAY, 0, -5 ) );
        	
        	paragraph = new Paragraph( "ENCABEZADO EFKDFGKFDGF.\n", fontNormal );
        	paragraph.setAlignment( Element.ALIGN_CENTER );
        	
        	document.add( paragraph );
        	
        	paragraph = new Paragraph( "fjeflkwe sdfklweflwe wfe fw efw efwfewfw efwe fwef wef wefwef efw wefw efwee "
        		+ "hogjoifweoi jdjosjdosd sdjksdjksd  skdjskdsk dksjdks ksdjksdj ksdjks ddksjdksjdksjdkk ksdjks kdd "
        		+ "hogjoifweoi jdjosjdosd sdjksdjksd  skdjskdsk dksjdks ksdjksdj ksdjks ddksjdksjdksjdkk ksdjks kdd "
        		+ "hogjoifweoi jdjosjdosd sdjksdjksd  skdjskdsk dksjdks ksdjksdj ksdjks ddksjdksjdksjdkk ksdjks kdd "
        		+ "hogjoifweoi jdjosjdosd sdjksdjksd  skdjskdsk dksjdks ksdjksdj ksdjks ddksjdksjdksjdkk ksdjks kdd "
        		+ "hogjoifweoi jdjosjdosd sdjksdjksd  skdjskdsk dksjdks ksdjksdj ksdjks ddksjdksjdksjdkk ksdjks kdd "
        		+ "hogjoifweoi jdjosjdosd sdjksdjksd  skdjskdsk dksjdks.", fontGray );
        	paragraph.setAlignment( Element.ALIGN_CENTER );
        	
        	document.add( paragraph );
        	
        	document.add( new LineSeparator( 0.5f, 100, BaseColor.GRAY, 0, -5 ) );
        	
        	document.close();
        	
        	return stream.toByteArray();
        	
        } catch( Exception err ) {
        	throw new Exception( "Error." );
        }
    }
}
