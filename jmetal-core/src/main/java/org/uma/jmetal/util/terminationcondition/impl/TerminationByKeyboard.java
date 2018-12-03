package org.uma.jmetal.util.terminationcondition.impl;

import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.terminationcondition.TerminationCondition;

import java.util.Map;
import java.util.Scanner;

public class TerminationByKeyboard implements TerminationCondition {
  public boolean keyHit ;

  public TerminationByKeyboard() {
    keyHit = false;

    KeyboardReader keyboardReader = new KeyboardReader(this) ;
    keyboardReader.start();
  }

  @Override
  public boolean check(Map<String, Object> algorithmStatusData) {
    if (keyHit) {
      JMetalLogger.logger.info("Evaluations: " + (int)algorithmStatusData.get("EVALUATIONS"));
    }
    return keyHit ;
  }

  private class KeyboardReader extends Thread {
    private TerminationByKeyboard terminationByKeyboard ;

    public KeyboardReader(TerminationByKeyboard terminationByKeyboard) {
      this.terminationByKeyboard = terminationByKeyboard ;
    }


    @Override
    public void run() {
      System.out.println("Press any key and hit return") ;
      Scanner scanner = new Scanner(System.in);

      scanner.nextLine() ;

      terminationByKeyboard.keyHit = true ;
    }
  }
}
