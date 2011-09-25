package org.tksk.xfxcsv;

public class Field implements FieldName {
	private final String name;
	private Context context = Context.NEUTRAL;
	private FieldDomain fieldDomain = FieldDomain.NEUTRAL;
	private Formatter formatter;

	/* package private */
	Field(String name) {
		this.name = name;
	}

	public String name() {
		return this.name;
	}

	/* package private */
	Field context(Context context) {
		this.context = context;
		return this;
	}

	public Context context() {
		return context;
	}

	public FieldDomain fieldDomain() {
		return this.fieldDomain;
	}

	public Field fieldDomain(FieldDomain fieldDomain) {
		this.fieldDomain = fieldDomain;
		return this;
	}

	public Field formater(Formatter formater) {
		this.formatter = formater;
		return this;
	}

	public Formatter formatter() {
		return this.formatter;
	}

	public static interface FieldDomain {
		static final FieldDomain NEUTRAL = new FieldDomain() {
			@Override public String name() { return "#NEUTRAL"; }
		};

		String name();
	}

}