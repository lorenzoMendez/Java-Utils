package com.lorenzo.soap.commons;

/*
 * My Singleton Definition
 * 
 * */
public class ConfigurationLoader {
	private String value1;
	private String value2;
	private String value3;
	
	private static volatile ConfigurationLoader instance;
	
	private ConfigurationLoader() { }
	
	public static ConfigurationLoader getInstance() {
		if( instance == null ) {
			synchronized( ConfigurationLoader.class ) {
				if( instance == null ) {
					instance = new ConfigurationLoader();
				}
			}
		}
		return instance;
	}

	/**
	 * @return the value1
	 */
	public String getValue1() {
		return value1;
	}

	/**
	 * @param value1 the value1 to set
	 */
	public void setValue1(String value1) {
		this.value1 = value1;
	}

	/**
	 * @return the value2
	 */
	public String getValue2() {
		return value2;
	}

	/**
	 * @param value2 the value2 to set
	 */
	public void setValue2(String value2) {
		this.value2 = value2;
	}

	/**
	 * @return the value4
	 */
	public String getValue3() {
		return value3;
	}

	/**
	 * @param value4 the value4 to set
	 */
	public void setValue3(String value4) {
		this.value3 = value4;
	}
}
