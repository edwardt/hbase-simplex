package com.xebia.simplex;

import org.apache.hadoop.util.ProgramDriver;

/**
 * Created by IntelliJ IDEA.
 * User: Maarten Winkels
 * Date: 9-1-11
 * Time: 12:47
 */
public class SimplexDriver extends ProgramDriver {

    private SimplexDriver() throws Throwable {
        addClass("load", LoadTool.class, "Loads a MPS file into an HBase database");
    }

    public static void main(String[] args) throws Throwable {
        new SimplexDriver().driver(args);
    }
}
