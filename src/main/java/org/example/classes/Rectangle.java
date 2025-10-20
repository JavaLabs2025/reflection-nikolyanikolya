package org.example.classes;

import org.example.Generatable;

@Generatable
public final class Rectangle extends AbstractShape implements Shape, SealedShape {
  private double length;
  private double width;

  public Rectangle(double length, double width) {
    super("Rectangle");
    this.length = length;
    this.width = width;
  }

  @Override
  public double getArea() {
    return length * width;
  }

  @Override
  public double getPerimeter() {
    return 2 * (length + width);
  }
}