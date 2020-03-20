package com.lorenzo.soap.service;

import javax.persistence.EntityManager;
import javax.persistence.Table;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public class SOAPConnector extends WebServiceGatewaySupport {
	
	private static final String CLASS_MODEL_LOCATION = "com.lorenzo.soap.model.";
	
	@Autowired
	private EntityManager em;
	
	public Object callWebService( String urlService, Object request) throws Exception {
		try {
			
			return getWebServiceTemplate().marshalSendAndReceive( urlService, request );
			
		} catch( Exception err ) {
			throw new Exception( "Error al consumir el servicio: " + urlService );
		}
	}
	
	// Retreive table name using persist objet mapping name
	public String getTableName( String cataloType ) throws Exception {
		try {
			// Full path is necessary
			Class<?> entityClass = Class.forName( CLASS_MODEL_LOCATION + cataloType.trim() );
			
			Metamodel meta = em.getMetamodel();
		    
			EntityType<?> entityType = meta.entity( entityClass );
		    
		    Table t = entityClass.getAnnotation(Table.class);
		    
		    String tableName = (t == null) ? entityType.getName().toUpperCase() : t.name();
		    
		    return tableName;
		    
		} catch( Exception err ) {
			throw new Exception( "Error al recuperar el nombre de la tabla." );
		}
	}
}