package org.example.classes;

import org.example.Generatable;

@Generatable
public sealed interface SealedShape permits Rectangle, Triangle {
  double getArea();
}
