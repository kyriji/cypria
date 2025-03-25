package dev.kyriji.commonmc.cypria.misc;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ReflectionUtils {
	public static <T> List<T> initPackage(String packageName, Class<T> type) {
		System.out.println("attempting to initialize package: " + packageName);
		List<T> instances = new ArrayList<>();

		try {
			File pluginsDir = new File("plugins");
			if (pluginsDir.exists() && pluginsDir.isDirectory()) {
				for (File file : pluginsDir.listFiles((dir, name) -> name.endsWith(".jar"))) {
					try (JarFile jarFile = new JarFile(file)) {
						Enumeration<JarEntry> entries = jarFile.entries();
						String path = packageName.replace('.', '/');

						while (entries.hasMoreElements()) {
							JarEntry entry = entries.nextElement();
							String entryName = entry.getName();

							if (entryName.startsWith(path + "/") && entryName.endsWith(".class")) {
								String className = entryName.substring(0, entryName.length() - 6).replace('/', '.');
								instances.addAll(instantiateClass(className, type));
							}
						}
					} catch (Exception e) {
						System.out.println("error scanning jar " + file.getName() + ": " + e.getMessage());
					}
				}
			}
		} catch (Exception e) {
			System.out.println("error during classpath scan: " + e);
		}

		System.out.println("instantiated " + instances.size() + " class" + (instances.size() == 1 ? "" : "es") +
				" in package " + packageName);
		return instances;
	}

	private static <T> List<T> instantiateClass(String className, Class<T> type) {
		List<T> instances = new ArrayList<>();

		try {
			Class<?> clazz = Class.forName(className);

			if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()) || !type.isAssignableFrom(clazz)) {
				System.out.println("skipping " + className + " (not compatible with " + type.getName() + ")");
				return instances;
			}

			Object instance = clazz.getDeclaredConstructor().newInstance();
			instances.add(type.cast(instance));
			System.out.println("instantiated: " + className);
		} catch (NoSuchMethodException e) {
			System.out.println("no empty constructor found for " + className);
		} catch (Exception e) {
			System.out.println("error instantiating " + className + ": " + e.getMessage());
		}

		return instances;
	}
}