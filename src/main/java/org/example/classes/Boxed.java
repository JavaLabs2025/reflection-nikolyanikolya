package org.example.classes;

public class Boxed<T> {
  public final T boxedValue;

  public Boxed(T value) {
    boxedValue = value;
  }

  @Override
  public String toString() {
    return boxedValue.toString();
  }
}
