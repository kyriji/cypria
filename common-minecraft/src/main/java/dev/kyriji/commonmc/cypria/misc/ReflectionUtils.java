package dev.kyriji.commonmc.cypria.misc;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtils {
	public static <T> List<T> initPackage(String packageName, Class<T> type) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String path = packageName.replace('.', '/');

		URL resource = classLoader.getResource(path);
		if (resource == null) {
			System.out.println("package not found: " + packageName);
			return new ArrayList<>();
		}

		File directory = new File(resource.getFile());
		List<T> instances = recursivelyInstantiate(directory, packageName, type);

		System.out.println("found " + instances.size() + " classes in package " + packageName);
		return instances;
	}

	private static <T> List<T> recursivelyInstantiate(File directory, String packageName, Class<T> type) {
		List<T> instances = new ArrayList<>();

		if (!directory.exists()) {
			System.out.println("directory does not exist: " + directory.getAbsolutePath());
			return instances;
		}

		File[] files = directory.listFiles();
		if (files == null) {
			System.out.println("unable to list files in directory: " + directory.getAbsolutePath());
			return instances;
		}

		for (File file : files) {
			if (file.isDirectory()) {
				instances.addAll(recursivelyInstantiate(file,
						packageName + "." + file.getName(), type));
			} else if (file.getName().endsWith(".class")) {
				String className = packageName + "." +
						file.getName().substring(0, file.getName().length() - 6);

				try {
					Class<?> clazz = Class.forName(className);

					if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()) ||
							!type.isAssignableFrom(clazz)) {
						System.out.println("skipping " + className + " (not compatible with " + type.getName() + ")");
						continue;
					}

					Object instance = clazz.getDeclaredConstructor().newInstance();
					instances.add(type.cast(instance));
					System.out.println("instantiated " + className);
				} catch (NoSuchMethodException e) {
					System.out.println("no no-args constructor found for " + className);
				} catch (Exception e) {
					System.out.println("error instantiating " + className + ": " + e.getMessage());
				}
			}
		}

		return instances;
	}
}