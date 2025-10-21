package org.example.classes;

import org.example.Generatable;

@Generatable
public interface Shape extends Figure {
    double getArea();
    double getPerimeter();
}