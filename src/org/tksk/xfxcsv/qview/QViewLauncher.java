package org.tksk.xfxcsv.qview;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tksk.xfxcsv.BaseForm;
import org.tksk.xfxcsv.Field;

public class QViewLauncher {

	private static final Logger logger = LoggerFactory.getLogger(QViewLauncher.class);

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

/*		Class<BaseForm> formClass = (Class<BaseForm>) cc.toClass();
		// (Class<BaseForm>) Class.forName("org.tksk.xfxcsv.test.FormA");

		Constructor<BaseForm> formConstructor = formClass.getConstructor(List.class);
		final List<Field> fields = new ArrayList<Field>();
		final BaseForm form = formConstructor.newInstance(fields);
*/

		final Server server = new Server(Integer.parseInt(Settings.qview$httpPort.value()));
		server.setGracefulShutdown(1000);
		Handler handler = new AbstractHandler() {
		    public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch)
		        throws IOException, ServletException
		    {
		        response.setContentType("text/html; charset=UTF-8");
		        request.setCharacterEncoding("UTF-8");
		        response.setStatus(HttpServletResponse.SC_OK);
		        
		        PrintWriter out = response.getWriter();
		        
		        if(target.equals("/admin/shutdown")) {
		        	try {
						server.stop();
					} catch (Exception e) {
						e.printStackTrace();
					}
			        ((Request)request).setHandled(true);
		        	return;
		        } else if(target.equals("/")) {
		        	out.println("<h1>Available forms:</h1><ul>");
		        	out.println("HINT: these are discovered on `qview.packages' in qview.properties");

			        for(Class<?> clazz : getClassesForTarget()) {
		        	//for(Class<?> clazz : getClassesForPackage(Package.getPackage("org.tksk.xfxcsv.test"))){
			        	String cname = clazz.getCanonicalName();
			        	out.println(String.format("<li><a href='/peek/%1$s'>%1$s</a></li>\n", cname));
			        }
			        out.println("</ul>");
			        out.println("<h1>admin menu</h1>");
			        out.println("<ul><li><a href='/admin/shutdown'>shutdown</a></li></ul>");

		        } else if(target.startsWith("/peek/")) {

			        String cname = target.substring("/peek/".length());
			        out.println("<h1>peek: " + cname + "</h1>");
			        out.println("<table><tr>");

					try {
						@SuppressWarnings("unchecked")
						Class<BaseForm> formClass = (Class<BaseForm>) Class.forName(cname);
						Constructor<BaseForm> formConstructor = formClass.getConstructor(List.class);
						final List<Field> fields = new ArrayList<Field>();
						final BaseForm form = formConstructor.newInstance(fields);

						for(Field f : form) {
							out.println(String.format("<th class='%s'>%s</th>", f.context().name(), f.name()));
						}

						out.println("</tr></table>");
					} catch (ClassNotFoundException e) {
						logger.error("ClassNotFoundException", e);
					} catch (SecurityException e) {
						logger.error("SecurityException", e);
					} catch (NoSuchMethodException e) {
						logger.error("NoSuchMethodException", e);
					} catch (IllegalArgumentException e) {
						logger.error("IllegalArgumentException", e);
					} catch (InstantiationException e) {
						logger.error("InstantiationException", e);
					} catch (IllegalAccessException e) {
						logger.error("IllegalAccessException", e);
					} catch (InvocationTargetException e) {
						logger.error("InvocationTargetException", e);
					}
		        }

		        ((Request)request).setHandled(true);
		    }
		};

		server.setHandler(handler);
		server.start();

	}

	private static List<Class<?>> getClassesForTarget() {
		List<Class<?>> ret = new ArrayList<Class<?>>();
		for(String pkg : Settings.qview$packages.value().split(":")) {
			//System.out.println("pkg: " + pkg);
			//System.out.println("pkg=> " + Package.getPackage(pkg));
//			for(Package p : Package.getPackages()) System.out.println(p);

			try {
				Class.forName(pkg + ".package-info");
				for(Class<?> clazz : getClassesForPackage(Package.getPackage(pkg))) {
					if(BaseForm.class.isAssignableFrom(clazz)) {
						ret.add(clazz);
					}
				}
			} catch (ClassNotFoundException e) {
				logger.error("cannot load package-info: {}", pkg);
				System.err.println("cannot load package-info: " + pkg);
			}
		}

		return ret;
	}

	private static List<Class<?>> getClassesForPackage(Package pkg) {
	    String pkgname = pkg.getName();
	    ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
	    // Get a File object for the package
	    File directory = null;
	    String fullPath;
	    String relPath = pkgname.replace('.', '/');
	    logger.debug("ClassDiscovery: Package: {} becomes Path: {}", pkgname, relPath);
	    URL resource = ClassLoader.getSystemClassLoader().getResource(relPath);
	    logger.debug("ClassDiscovery: Resource = {}");
	    if (resource == null) {
	        throw new RuntimeException("No resource for " + relPath);
	    }
	    fullPath = resource.getFile();
	    logger.debug("ClassDiscovery: FullPath = {}", resource);

	    try {
	        directory = new File(resource.toURI());
	    } catch (URISyntaxException e) {
	        throw new RuntimeException(pkgname + " (" + resource
	        		+ ") does not appear to be a valid URL / URI.  Strange, since we got it from the system...", e);
	    }
	    logger.debug("ClassDiscovery: Directory = {}", directory);

	    if (directory.exists()) {
	        // Get the list of the files contained in the package
	        String[] files = directory.list();
	        for (int i = 0; i < files.length; i++) {
	            // we are only interested in .class files
	            if (files[i].endsWith(".class")) {
	                // removes the .class extension
	                String className = pkgname + '.' + files[i].substring(0, files[i].length() - 6);
	                logger.debug("ClassDiscovery: className = {}", className);
	                try {
	                    classes.add(Class.forName(className));
	                } 
	                catch (ClassNotFoundException e) {
	                    throw new RuntimeException("ClassNotFoundException loading " + className);
	                }
	            }
	        }
	    }
	    else {
	        try {
	            String jarPath = fullPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
	            JarFile jarFile = new JarFile(jarPath);
	            Enumeration<JarEntry> entries = jarFile.entries();
	            while(entries.hasMoreElements()) {
	                JarEntry entry = entries.nextElement();
	                String entryName = entry.getName();
	                if(entryName.startsWith(relPath) && entryName.length() > (relPath.length() + "/".length())) {
	                	logger.debug("ClassDiscovery: JarEntry: {}", entryName);
	                    String className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
	                    logger.debug("ClassDiscovery: className = {}", className);
	                    try {
	                        classes.add(Class.forName(className));
	                    } 
	                    catch (ClassNotFoundException e) {
	                        throw new RuntimeException("ClassNotFoundException loading " + className);
	                    }
	                }
	            }
	        } catch (IOException e) {
	            throw new RuntimeException(pkgname + " (" + directory + ") does not appear to be a valid package", e);
	        }
	    }

	    return classes;
	}

}
