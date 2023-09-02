package io.test;
import io.unfish.txt;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

public final class test {

    public static void main(String[] arguments){
        File conf = new File("config.txt");

        txt t = new txt(conf.toPath());
        /*
        for(Map.Entry<String, String> entry: t.book.entrySet())
        {
        	System.out.println(entry.getKey() + "  --> " +entry.getValue());
        }
         
         */
        
        String value1[] = {"web", "minecraft","pingtest", "pagespeed-test"};
        t.set("public#server#name",null, Arrays.asList(value1), true);
        
        
    }
}
