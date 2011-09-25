package org.tksk.xfxcsv;

import java.util.Iterator;
import java.util.List;

public class BaseForm implements Iterable<Field> {
	private final List<Field> fields; // = new ArrayList<Field>();
	private Context currentContext = null;

	protected Field field(String name) {
		Field field = new Field(name);
		fields.add(field);

		return field;
	}

	protected Context context(String name) {
		this.currentContext = new Context(name);

		return this.currentContext;
	}

	public void registerBinder(String fieldName, String f) {
		
	}

	public BaseForm(List<Field> fields) {
		this.fields = fields;
	}

	@Override
	public Iterator<Field> iterator() {
		return this.fields.iterator();
	}
}
