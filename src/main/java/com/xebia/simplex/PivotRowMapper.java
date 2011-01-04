package com.xebia.simplex;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;


/**
 * Turns lines from a text file into (word, 1) tuples.
 */
public class PivotRowMapper extends Mapper<LongWritable, Text, LongWritable, Text> {

    int pivotRow = 1;
    int rowCount = 4;

    protected void map(LongWritable offset, Text value, Context context) throws IOException, InterruptedException {
        if (offset.get() == pivotRow) {
            for (long l = 0; l < rowCount; l++) {
                if (l != pivotRow) {
                    context.write(new LongWritable(l), new Text("P," + value));
                }
            }
        }
        context.write(new LongWritable(offset.get()), new Text(value));
    }
}
