/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package omnibot;

import omnibot.WikiPage;

/**
 * Interface that defines a library of filter wikipages
 * There should be implemented a few of public static final
 * methods:
 * String version() returning a code version
 * String[] constructorArguments() returning list of arguments of a constructor
 * String hint() returning a hint what the constructor arguments mean
 * @author Pavel Dusek
 */
public interface Filter {
    //public String version();
    //public String[] constructorArguments();
    //public String hint();
    public String getName();
    public boolean filter(WikiPage page);
}
