package com.github.easydoc;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

import org.apache.commons.lang.ClassUtils;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jfrog.maven.annomojo.annotations.MojoParameter;

import com.github.easydoc.exception.EasydocFatalException;

public class CommandLineMojoUtils {
	
	public static abstract class ArgException extends Exception {
		private static final long serialVersionUID = 6015239894637628421L;

		public ArgException(String msg) {
			super(msg);
		}
	}
	
	public static class InvalidArgException extends ArgException {
		private static final long serialVersionUID = 6537755470692441678L;

		public InvalidArgException(String arg) {
			super("Illegal argument: " + arg);
		}
	}
	
	public static class RequiredArgException extends ArgException {
		private static final long serialVersionUID = 4951693249353819692L;

		public RequiredArgException(String arg) {
			super("Missing required argument: " + arg);
		}
	}

	public static void injectMojoProperties(EasydocMojo mojo, String[] args) throws ArgException {
		try {
			Set<Field> injectedFields = new HashSet<Field>();
			
			for(String arg : args) {
				try {
					String[] nameAndValue = arg.split("=");
					if(nameAndValue.length != 2) throw new InvalidArgException(arg);
					
					Field field = mojo.getClass().getDeclaredField(nameAndValue[0]);
					field.setAccessible(true);
					MojoParameter mojoParameterAnno = field.getAnnotation(MojoParameter.class);
					if(mojoParameterAnno == null) throw new InvalidArgException(arg);
					
					injectValue(mojo, field, nameAndValue[1]);
					
					injectedFields.add(field);
				}
				catch(NoSuchFieldException e) {
					throw new InvalidArgException(arg);
				}
			}
			
			//check for required properties
			for(Field field : mojo.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				if(injectedFields.contains(field)) continue;
				
				MojoParameter mojoParameterAnno = field.getAnnotation(MojoParameter.class);
				if(mojoParameterAnno == null) continue;
				
				String expression = mojoParameterAnno.expression();
				if(expression != null && !expression.isEmpty()) {
					injectValue(
							mojo, 
							field, 
							expression
							.replaceAll("\\$\\{basedir\\}", Matcher.quoteReplacement(new File("").getAbsolutePath()))
							.replaceAll("\\$\\{project.build.directory\\}", "build")
							.replaceAll("/", Matcher.quoteReplacement(File.separator))
					);
				}
				else {
					if(mojoParameterAnno.required()) {
						throw new RequiredArgException(field.getName());
					}
				}
			}
		}
		catch(IllegalAccessException e) {
			throw new EasydocFatalException(e);
		}
		catch(IOException e) {
			throw new EasydocFatalException(e);
		}
	}

	private static void injectValue(EasydocMojo mojo, Field field, String value) throws IllegalAccessException, IOException, InvalidArgException {
		Class<?> type = field.getType();
		if(type.equals(File.class)) {
			field.set(mojo, new File(value));
		}
		else if(type.equals(MavenProject.class) || type.equals(MavenSession.class)) {
			//skip these fields
		}
		else if(isPrimitive(type)) {
			field.set(mojo, value);			
		}
		else {
			//parse object from JSON
			try {
				ObjectMapper om = new ObjectMapper();
				field.set(
						mojo, 
						om.readValue(value.getBytes(), type)
				);
			} catch(JsonProcessingException e) {
				throw new InvalidArgException(field.getName());
			}
		}
		//TODO: handle lists
	}

	private static boolean isPrimitive(Class<?> type) {
		return type.equals(String.class) || 
				type.isPrimitive() || 
				ClassUtils.wrapperToPrimitive(type) != null;
	}
}
