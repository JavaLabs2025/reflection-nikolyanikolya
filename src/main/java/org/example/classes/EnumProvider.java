package org.example.classes;

import org.example.Generatable;

@Generatable
public class EnumProvider {
  public final EnumExample value;

  public EnumProvider(EnumExample value) {
    this.value = value;
  }
}
