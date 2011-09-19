package org.tksk.xfxcsv.sql;

import org.tksk.xfxcsv.FieldName;
import org.tksk.xfxcsv.Formatter;

public final class PredefFormatters {
	private PredefFormatters(){}
	
	public Formatter getOrElse(final FieldName name, final String defaultValue) {
		return new Formatter() {
			@Override public String format(Object object) throws RuntimeException {
				if(object instanceof ResultSet) {
					ResultSet rs = (ResultSet) object;
					return rs.getOrElse(name.name(), defaultValue);
				} else {
					throw new RuntimeException("needed an instance of ResultSet");
				}
			}
		};
	}
}
