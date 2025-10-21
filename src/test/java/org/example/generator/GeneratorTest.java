package org.example.generator;

import org.example.classes.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GeneratorTest {

  @Test
  public void generateValueOfType_EnumExample() {
    var gen = new Generator();
    EnumExample generated = gen.generateValueOfType(EnumExample.class);
    System.out.println("Generated");
    System.out.println(generated);
  }

  @Test
  public void generateValueOfType_EnumProvider() {
    var gen = new Generator();
    EnumProvider generated = gen.generateValueOfType(EnumProvider.class);
    System.out.println("Generated");
    System.out.println(generated.value);
  }

  @Test
  public void generateValueOfType_Shape() {
    var gen = new Generator();
    Shape generated = gen.generateValueOfType(Shape.class);
    System.out.println("Generated");
    System.out.println(generated.getArea());
  }

  @Test
  public void generateValueOfType_Figure() {
    var gen = new Generator();
    Figure generated = gen.generateValueOfType(Figure.class);
    System.out.println("Generated");
  }

  @Test
  public void generateValueOfType_SealedShape() {
    var gen = new Generator();
    SealedShape generated = gen.generateValueOfType(SealedShape.class);
    System.out.println("Generated");
    System.out.println(generated.getArea());
  }

  @Test
  public void generateValueOfType_AbstractShape() {
    var gen = new Generator();
    AbstractShape generated = gen.generateValueOfType(AbstractShape.class);
    System.out.println("Generated");
    System.out.println(generated.getArea());
  }

  @Test
  public void generateValueOfType_Triangle() {
    var gen = new Generator();
    Shape generated = gen.generateValueOfType(Triangle.class);
    System.out.println("Generated");
    System.out.println(generated.getArea());
  }

  @Test
  public void generateValueOfType_Rectangle() {
    var gen = new Generator();
    Shape generated = gen.generateValueOfType(Rectangle.class);
    System.out.println("Generated");
    System.out.println(generated.getArea());
  }

  @Test
  public void generateValueOfType_Example() {
    var gen = new Generator();
    Example generated = gen.generateValueOfType(Example.class);
    System.out.println("Generated");
    System.out.println(generated);
  }

  @Test
  public void generateValueOfType_Boxed() {
    var gen = new Generator();
    assertThatThrownBy(() ->
      gen.generateValueOfType(Boxed.class)
    )
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Generating of class org.example.classes.Boxed is not supported");
  }

  @Test
  public void generateValueOfType_Product() {
    var gen = new Generator();
    Product generated = gen.generateValueOfType(Product.class);
    System.out.println("Generated");
    System.out.println(generated.getName());
  }

  @Test
  public void generateValueOfType_Cart() {
    var gen = new Generator();
    Cart generated = gen.generateValueOfType(Cart.class);
    System.out.println("Generated");
    System.out.println(generated.getItems());
    generated.getItems().addFirst(null);
  }

  @Test
  public void generateValueOfType_List() {
    var gen = new Generator();
    assertThatThrownBy(() -> gen.generateValueOfType(List.class))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Generating of interface java.util.List is not supported");
  }

  @Test
  public void generateValueOfType_BinaryTreeNode() {
    var gen = new Generator();
    BinaryTreeNode generated = gen.generateValueOfType(BinaryTreeNode.class);
    System.out.println("Generated");
    System.out.println(generated);
  }

  @Test
  public void generateValueOfType_int() {
    var gen = new Generator();
    int generated = gen.generateValueOfType(int.class);
    System.out.println("Generated");
    System.out.println(generated);
  }

  @Test
  public void generateValueOfType_double() {
    var gen = new Generator();
    double generated = gen.generateValueOfType(double.class);
    System.out.println("Generated");
    System.out.println(generated);
  }

  @Test
  public void generateValueOfType_long() {
    var gen = new Generator();
    long generated = gen.generateValueOfType(long.class);
    System.out.println("Generated");
    System.out.println(generated);
  }

  @Test
  public void generateValueOfType_short() {
    var gen = new Generator();
    short generated = gen.generateValueOfType(short.class);
    System.out.println("Generated");
    System.out.println(generated);
  }

  @Test
  public void generateValueOfType_float() {
    var gen = new Generator();
    float generated = gen.generateValueOfType(float.class);
    System.out.println("Generated");
    System.out.println(generated);
  }

  @Test
  public void generateValueOfType_byte() {
    var gen = new Generator();
    float generated = gen.generateValueOfType(byte.class);
    System.out.println("Generated");
    System.out.println(generated);
  }

  @Test
  public void generateValueOfType_String() {
    var gen = new Generator();
    assertThatThrownBy(() -> gen.generateValueOfType(String.class))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Generating of class java.lang.String is not supported");
  }

  @Test
  public void generateValueOfType_array_of_objects() {
    var gen = new Generator();
    String[] generated = gen.generateValueOfType(String[].class);
    System.out.println("Generated");
    System.out.println(Arrays.toString(generated));
  }

  @Test
  public void generateValueOfType_array_of_primitives() {
    var gen = new Generator();
    int[] generated = gen.generateValueOfType(int[].class);
    System.out.println("Generated");
    System.out.println(Arrays.toString(generated));
  }

  @Test
  public void generateValueOfType_Integer() {
    var gen = new Generator();
    assertThatThrownBy(() -> gen.generateValueOfType(Integer.class))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Generating of class java.lang.Integer is not supported");
  }

  @Test
  public void generateValueOfType_Double() {
    var gen = new Generator();
    assertThatThrownBy(() -> gen.generateValueOfType(Double.class))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Generating of class java.lang.Double is not supported");
  }
}
