package org.example.classes;

import org.example.Generatable;

public class Boxed<T extends Number> {
  public final T boxedValue;

  public Boxed(T value) {
    boxedValue = value;
  }

  @Override
  public String toString() {
    return boxedValue.toString();
  }
}
