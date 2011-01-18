package com.xebia.mps;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.FractionFormat;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.*;

/**
 * Captures the MetaData of an MPS file.
 */
public class MPSMetaData {

    private String name;
    private String target;
    private int rowCount;
    private int slackCount;
    private BufferedWriter out;
    private SortedSet<String> columns = new TreeSet<String>();
    private Set<String> toReverse = new HashSet<String>();

    public MPSMetaData() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void registerRow(char type, String name) {
        switch (type) {
            case 'N' : setTarget(name); break;
            case 'E' : addEquation(name); break;
            case 'L' : addRestriction(name); break;
            case 'G' : addReverseRestriction(name); break;
            default : //Cannot happen.
        }
    }

    private void addReverseRestriction(String name) {
        this.toReverse.add(name);
        addRestriction(name);
    }

    private void addRestriction(String name){
        this.rowCount++;
        this.slackCount++;
        columns.add("SLACK" + slackCount);
        writeSlack(name);
    }

    private void writeSlack(String name) {
        try {
            out.write(String.format("%1$s\tSLACK%2$d\t1", name, slackCount));
            out.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Error while writing to temporary file.",e);
        }
    }

    private void addEquation(String name) {
        this.rowCount++;
    }

    private void setTarget(String name) {
        if (this.target == null) {
            this.target = name;
        } else {
            MPSReader.LOG.info("Ignoring additional function: " + name);
        }
    }

    public void registerValue(String column, String row, double value) {
        Fraction fraction = toFraction(value);
        if (toReverse.contains(row)) {
            fraction = fraction.negate();
        }
        columns.add(column);
        writeToFile(row, column, fraction);
    }

    private Fraction toFraction(double value) {
        try {
                return new Fraction(value);
            } catch (ConvergenceException e) {
                throw new RuntimeException("Could not convert number to fraction: " + value, e);
            }
    }

    private void writeToFile(String row, String column, Fraction fraction) {
        try {
            out.write(String.format("%1$s\t%2$s\t%3$s", row, column, fractionToString(fraction)));
            out.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Error while writing to temporary file.",e);
        }
    }

    public void registerRightHandSide(String column, String row, double value) {
        columns.add(column);
        writeToFile(row, column, toFraction(value));
    }

    public void registerBound(MPSBoundType type, String column, String row, double value) {

    }

    private String fractionToString(Fraction fraction) {
        return fraction.getDenominator() == 1 ? Integer.toString(fraction.getNumerator()) : FractionFormat.getImproperInstance().format(fraction);
    }

    public void end() {
        try {
            out.close();
        } catch (IOException e) {
            throw new RuntimeException("Unable to close stream.", e);
        }
    }

    public void setDataSink(Writer out) {
        this.out = new BufferedWriter(out);
    }

    public String getName() {
        return name;
    }

    public String getTarget() {
        return target;
    }

    public SortedSet<String> getColumns() {
        return columns;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getSlackCount() {
        return slackCount;
    }

}
