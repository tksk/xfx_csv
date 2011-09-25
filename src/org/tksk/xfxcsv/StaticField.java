package org.tksk.xfxcsv;


public class StaticField extends Field {

	/* package private */
	StaticField(String name, final String text) {
		super(name);

		this.formater(new Formatter() {
			@Override public String format(Object object) throws RuntimeException {
				return text;
			}
		});
	}
}
