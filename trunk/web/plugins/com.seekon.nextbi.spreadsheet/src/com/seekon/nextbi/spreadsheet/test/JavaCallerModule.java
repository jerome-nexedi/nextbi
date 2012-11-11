package com.seekon.nextbi.spreadsheet.test;

import com.caucho.quercus.env.Env;
import com.caucho.quercus.module.AbstractQuercusModule;

public class JavaCallerModule extends AbstractQuercusModule{

  public String getGreetStringFromModule(Env env, String userName){
    String greetString = "Hello " + userName + ", this is JavaCallerModule.";
    System.out.println(greetString);
    return greetString;
  }
}
