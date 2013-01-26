/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package omnibot.re;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import omnibot.WikiPage;

/**
 * Class that takes care of regex searching in a {@link WikiPage}
 * @author Pavel Dusek
 */
public class Re implements omnibot.Filter {
    String regex;
    WikiPage page;
    
    public Re(String regex) {
        this.regex = regex;
    }
  
    /**
     * @return Version of this class code
     */
    public static final String version() {
        return "1.0";
    }
    
    /**
     * @return list of argument types for a constructor
     */
    public static String[] constructorArguments() {
        String[] arguments = {"String"};
        return arguments;
    }
    
    /**
     * @return hint what argument should be passed to method {@link Re.filter}
     */
    public static String hint() {
        return "Regulární výraz pro hledání v textu:";
    }
    
    public String getName() {
        return "re: " + regex;
    }

    public boolean filter(WikiPage page) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(page.getPageText());
        return m.find();
    }
}
