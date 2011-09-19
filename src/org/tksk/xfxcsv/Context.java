package org.tksk.xfxcsv;

import org.tksk.xfxcsv.Field.FieldDomain;

public class Context {
	/* package private */
	static final Context NEUTRAL = new Context("#NEUTRAL");

	private final String name;

	/* package private */
	Context(String name) {
		this.name = name;
	}

	public String name() {
		return this.name;
	}

	public Context fields(FieldDomain fieldDomain, Field ... fields) {
		for(Field field : fields) {
			field.context(this);
			field.fieldDomain(fieldDomain);
		}
		return this;
	}

	public Context fields(FieldDomainContent fieldDomainContent) {
		for(Field field : fieldDomainContent.body()) {
			field.context(this);
			field.fieldDomain(fieldDomainContent);
		}
		return this;
	}

	public static interface FieldDomainContent extends FieldDomain {
		// String name(); // inherited
		Iterable<Field> body();
	}
}