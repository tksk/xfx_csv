package org.tksk.xfxcsv.test;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;


public class BaseFormTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		ClassPool cp = ClassPool.getDefault();
        
		CtClass cc = cp.get("org.tksk.xfxcsv.test.FormA");
        //CtClass superClass = cc.getSuperclass();
        CtClass ctcList = cp.get("java.util.List");

        CtConstructor constructor = new CtConstructor(new CtClass[]{ctcList}, cc);
        constructor.setBody("{super($1);}");
        //cc.addConstructor(constructor);
        //cc.writeFile();

		//System.in.read();
		//server.stop();
	}

}
