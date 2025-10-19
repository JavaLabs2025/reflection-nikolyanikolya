package org.example.generator;

import java.io.File;
import java.lang.reflect.*;
import java.util.*;

public class Generator {

  public <T> T generateValueOfType(Class<T> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
    return clazz.cast(generateValueOfTypeInternal(clazz));
  }

  public Object generateValueOfTypeInternal(Class<?> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
    if (clazz.isInterface() || (Modifier.isAbstract(clazz.getModifiers()))) {
      return implementAndCreateInstanceOf(clazz);
    }
    return useClassConstructor(clazz);
  }

  public Object implementAndCreateInstanceOf(Class<?> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
    var permittedSubclasses = clazz.getPermittedSubclasses();
    if (permittedSubclasses != null) {
      int permittedSubclassIndex = new Random().nextInt(permittedSubclasses.length);
      return generateValueOfTypeInternal(permittedSubclasses[permittedSubclassIndex]);
    } else if (clazz.getClassLoader() != null) {
      Class<?>[] subclasses = searchForSubclassesOf(clazz);
      int subclassIndex = new Random().nextInt(subclasses.length);
      return generateValueOfTypeInternal(subclasses[subclassIndex]);
    } else {
      return null;
    }
  }

  public Object useClassConstructor(Class<?> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
    Constructor<?>[] constructors = clazz.getDeclaredConstructors();

    Constructor<?> selectedConstructor = Arrays.stream(constructors)
      .min(Comparator.comparingInt(Constructor::getParameterCount))
      .orElse(null);
    if (selectedConstructor != null) {
      Object[] args = Arrays.stream(selectedConstructor.getParameterTypes())
        .map(this::getDefaultValue)
        .toArray();

      return selectedConstructor.newInstance(args);
    }
    return null;
  }

  public Class<?>[] searchForSubclassesOf(Class<?> parentClass) {
    var classLoader = parentClass.getClassLoader();
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
      )
      .toArray(Class<?>[]::new)
    ;
  }

  public Object getDefaultValue(Class<?> clazz) {
    try {
      var random = new Random();
      if (clazz == String.class) return String.valueOf(random.nextInt());
      if (clazz.isArray()) {
        Object[] array = (Object[]) Array.newInstance(clazz.getComponentType(), 1);
        array[0] = generateValueOfType(clazz.getComponentType());
        return array;
      }
      if (!clazz.isPrimitive()) return generateValueOfType(clazz);
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
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
