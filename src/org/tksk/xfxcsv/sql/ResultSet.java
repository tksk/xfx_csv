package org.tksk.xfxcsv.sql;

import java.sql.SQLException;

public interface ResultSet {
	String get(String name) throws SQLException;
	String getOrElse(String name, String defaultValue) throws RuntimeException;

}
