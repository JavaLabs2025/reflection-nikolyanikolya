package org.example;


import org.example.classes.*;
import org.example.generator.Generator;

import java.util.List;

public class GenerateExample {
    public static void main(String[] args) {
        var gen = new Generator();
        try {
            EnumProvider generated = gen.generateValueOfType(EnumProvider.class);
            System.out.println("Generated");
            System.out.println(generated.value);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}