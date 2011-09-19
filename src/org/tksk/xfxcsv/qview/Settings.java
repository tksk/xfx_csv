package org.tksk.xfxcsv.qview;

import java.util.Properties;

public enum Settings {
	  _, // dummy
	  qview$httpPort("8080"),
	  qview$packages("")
	  ;

	  private static enum singleton {
	    instance
	    ;

	    public boolean hasError = false;

	    Properties props = new Properties(); {
	    	
	      try {
	    	  props.load(Settings.class.getResourceAsStream("/qview.properties"));
	      } catch (Exception e) {
	        hasError = true;
	      }
	    }
	  }

	  private final String defaultValue;
	  private final String key;

	  public String value() {
	    if(hasError()) throw new IllegalStateException("properties have not been loaded.");

	    return singleton.instance.props.getProperty(key, defaultValue);
	  }

	  public String key() {
	    return key;
	  }

	  public boolean hasError() {
	    return singleton.instance.hasError;
	  }

	  private Settings() {
	    this(null);
	  }

	  private Settings(String defaultValue) {
	    this.defaultValue = defaultValue;
	    this.key = this.name().replace('$', '.');
	  }
	}