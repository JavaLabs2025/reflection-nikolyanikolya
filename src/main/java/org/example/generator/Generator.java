package org.example.generator;

import org.example.Generatable;

import java.io.File;
import java.lang.reflect.*;
import java.util.*;

public class Generator {
  private static final int MAX_DEPTH = 10;

  public <T> T generateValueOfType(Class<T> clazz) {
    if (clazz.isPrimitive() || clazz.isArray() || clazz.isAnnotationPresent(Generatable.class)) {
      var value = generateValueOfTypeInternal(clazz, 0);
      if (clazz.isPrimitive()) return (T) value;
      return clazz.cast(value);
    } else {
      throw new IllegalArgumentException(String.format("Generating of %s is not supported", clazz));
    }
  }

  private Object generateValueOfTypeInternal(Class<?> clazz, int currentDepth) {
    System.out.printf("Generate instance of %s...\n", clazz);
    var random = new Random();
    if (clazz == String.class) return String.valueOf(random.nextInt());
    if (clazz.isArray()) {
      return considerArray(clazz, currentDepth);
    }
    if (clazz.isPrimitive()) {
      var primitiveValue = considerPrimitives(clazz, random);
      System.out.printf("Generated %s with value %s\n", clazz, primitiveValue);
      return primitiveValue;
    }
    if (clazz.isAssignableFrom(Collection.class)) {
      var collectionValue = considerCollection(clazz);
      if (collectionValue != null) {
        return collectionValue;
      }
    }
    if (clazz.isEnum()) {
      var enumConstants = clazz.getEnumConstants();
      var enumConstantsIndex = random.nextInt(enumConstants.length);
      return enumConstants[enumConstantsIndex];
    }
    if (currentDepth > MAX_DEPTH) {
      return null;
    }
    if (clazz.isInterface() || (Modifier.isAbstract(clazz.getModifiers()))) {
      return implementAndCreateInstanceOf(clazz, currentDepth);
    }
    try {
      return useClassConstructor(clazz, currentDepth);
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private Object useClassConstructor(Class<?> clazz, int currentDepth) throws InvocationTargetException, InstantiationException, IllegalAccessException {
    Constructor<?>[] constructors = clazz.getDeclaredConstructors();

    Constructor<?> selectedConstructor = Arrays.stream(constructors)
      .min(Comparator.comparingInt(Constructor::getParameterCount))
      .orElse(null);

    if (selectedConstructor != null) {
      var params = selectedConstructor.getParameterTypes();
      System.out.printf("Use %s class constructor with arguments: %s\n", clazz, Arrays.toString(params));
      Object[] args = Arrays.stream(params)
        .map(arg -> generateValueOfTypeInternal(arg, currentDepth + 1))
        .toArray();

      return selectedConstructor.newInstance(args);
    } else {
      throw new IllegalArgumentException(String.format("Generating of %s is not supported", clazz));
    }
  }

  private Object implementAndCreateInstanceOf(Class<?> clazz, int currentDepth) {
    var permittedSubclasses = clazz.getPermittedSubclasses();
    if (permittedSubclasses != null) {
      System.out.printf("Search permitted implementations of %s...\n", clazz);
      int permittedSubclassIndex = new Random().nextInt(permittedSubclasses.length);
      return generateValueOfTypeInternal(permittedSubclasses[permittedSubclassIndex], currentDepth + 1);
    } else {
      System.out.printf("Search for local implementations of %s...\n", clazz);
      Class<?>[] subclasses = searchForSubclassesOf(clazz);
      if (subclasses.length > 0) {
        int subclassIndex = new Random().nextInt(subclasses.length);
        System.out.printf("Found local implementation %s of %s\n", subclasses[subclassIndex], clazz);
        return generateValueOfTypeInternal(subclasses[subclassIndex], currentDepth + 1);
      } else {
        System.out.printf("Generate proxy implementation for %s...\n", clazz);
        if (clazz.isInterface()) {
          return Proxy.newProxyInstance(
            clazz.getClassLoader(),
            new Class<?>[]{clazz},
            (proxy, method, args) ->
              invocationHandler(currentDepth, proxy, method, args)
          );
        } else {
          throw new IllegalArgumentException(
            String.format("Cannot generate abstract %s implementation, interface expected", clazz)
          );
        }
      }
    }
  }

  private Class<?>[] searchForSubclassesOf(Class<?> parentClass) {
    var classLoader = parentClass.getClassLoader();
    if (classLoader == ClassLoader.getSystemClassLoader()) {
      var resource = parentClass.getPackageName().replace(".", "/");
      var url = classLoader.getResource(resource);
      var filesToTraverse = new File(url.getFile()).listFiles();
      return Arrays.stream(Objects.requireNonNull(filesToTraverse))
        .map(f -> {
          try {
            return classLoader.loadClass(parentClass.getPackageName() + "." + f.getName().replace(".class", ""));
          } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
          }
        })
        .filter(clazz ->
          Arrays.stream(clazz.getInterfaces()).anyMatch((cl) -> cl == parentClass)
            || (
            clazz.getSuperclass() != null &&
              Modifier.isAbstract(clazz.getSuperclass().getModifiers())
              && clazz.getSuperclass() == parentClass
          )
        )
        .toArray(Class<?>[]::new);
    } else {
      return new Class<?>[]{};
    }
  }

  private Object invocationHandler(int currentDepth, Object proxy, Method method, Object... args) {
    System.out.printf("Method %s (%s) call\n", method.getName(), Arrays.toString(args));
    var returnType = method.getReturnType();
    if (returnType != void.class) {
      return generateValueOfTypeInternal(method.getReturnType(), currentDepth + 1);
    } else {
      return null;
    }
  }

  private Object considerArray(Class<?> clazz, int currentDepth) {
    Object array = Array.newInstance(clazz.getComponentType(), 1);
    Array.set(array, 0, generateValueOfTypeInternal(clazz.getComponentType(), currentDepth + 1));
    return array;
  }

  private Object considerPrimitives(Class<?> clazz, Random random) {
    if (clazz == int.class) return random.nextInt();
    if (clazz == boolean.class) return random.nextBoolean();
    if (clazz == float.class) return random.nextFloat();
    if (clazz == double.class) return random.nextDouble();
    if (clazz == short.class) return (short) random.nextInt();
    if (clazz == long.class) return random.nextLong();
    if (clazz == byte.class) {
      var bytes = new byte[1];
      random.nextBytes(bytes);
      return bytes[0];
    }
    return null;
  }

  private Collection<?> considerCollection(Class<?> clazz) {
    if (clazz == List.class) return Collections.emptyList();
    if (clazz == Set.class) return Collections.emptySet();
    return null;
  }
}
