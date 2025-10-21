package org.example.classes;

import org.example.Generatable;

@Generatable
abstract public class AbstractShape implements Figure {
  protected final String description;

  public AbstractShape(String description) {
    this.description = description;
  }

  abstract public double getArea();
}
