package org.tksk.xfxcsv.test;

import java.util.List;

import org.tksk.xfxcsv.BaseForm;
import org.tksk.xfxcsv.Field;
import org.tksk.xfxcsv.test.appcommon.FieldDomain;

public class FormB extends BaseForm {
{
	context("aa")
		.fields(FieldDomain.CommonLeader,
			field("bb"),
			field("cc"))

		.fields(FieldDomain.Proper,
			field("mama"),
			field("papa"),
			field("jjjj"))

		.fields(FieldDomain.CommonFollower,
			field("mama"),
			field("papa")
		);

	registerBinder("bb", "");

	context("jiofae").fields(FieldDomain.CommonFollower,
		field("jiofew"),
		field("user")
	);
}
public FormB(List<Field> fields) { super(fields); }
}