package org.uma.jmetal.auto.parameterv2.param.catalogue;

import org.uma.jmetal.auto.parameterv2.param.RealParameter;

public class Probability extends RealParameter {
  private String name ;
  private String[] args ;

  public Probability(String args[], String name)  {
    super(0.0, 1.0) ;
    this.name = name ;
    this.args = args ;
  }

  @Override
  public RealParameter parse() {
    value = on("--"+name, args, Double::parseDouble);
    return this ;
  }

  @Override
  public String getName() {
    return name;
  }
}