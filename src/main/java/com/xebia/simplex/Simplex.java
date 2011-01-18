package com.xebia.simplex;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.lib.NLineInputFormat;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.IntSumReducer;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


/**
 * The famous MapReduce word count example for Hadoop.
 */
public class Simplex extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new Simplex(), args);
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception {
        String[] remainingArgs = new GenericOptionsParser(getConf(), args).getRemainingArgs();

        if (remainingArgs.length < 2) {
            System.err.println("Usage: Simplex <in> <out>");
            ToolRunner.printGenericCommandUsage(System.err);
            return 1;
        }

        Job job = new Job(getConf(), "Simplex");
        job.setJarByClass(getClass());

        job.setMapperClass(PivotRowMapper.class);

//        job.setCombinerClass(IntSumReducer.class);
//        job.setReducerClass(IntSumReducer.class);

        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(remainingArgs[0]));
        FileOutputFormat.setOutputPath(job, new Path(remainingArgs[1]));

        job.setInputFormatClass(TextInputFormat.class);

        boolean success = job.waitForCompletion(true);

        return success ? 0 : 1;
    }
}
