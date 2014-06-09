package com.omartech.gossip.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class DLDEConfiguration {

    public static PairValue getInstance(String path) {
        InputStream is = DLDEConfiguration.class.getClassLoader().getResourceAsStream(path);
        try {
            List<String> lines = IOUtils.readLines(is);
            for(String line : lines){
                if(line.contains(":")){
                    String key = line.substring(0, line.indexOf(":"));
                    String value = line.substring(line.indexOf(":"));
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
