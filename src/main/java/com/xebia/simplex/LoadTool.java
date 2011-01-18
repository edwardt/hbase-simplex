package com.xebia.simplex;

import com.xebia.mps.MPSMetaData;
import com.xebia.mps.MPSReader;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Maarten Winkels
 * Date: 9-1-11
 * Time: 12:49
 */
public class LoadTool extends Configured implements Tool {

    public static void main(String[] args) throws Throwable {
        System.exit(ToolRunner.run(new LoadTool(), args));
    }

    @Override
    public int run(String[] args) throws Exception {
        String[] remainingArgs = new GenericOptionsParser(getConf(), args).getRemainingArgs();

        if (remainingArgs.length < 1) {
            System.err.println("Usage: LoadTool <in>");
            ToolRunner.printGenericCommandUsage(System.err);
            return 1;
        }

        File outputFile = new File(remainingArgs[0] + ".reordered");
        MPSMetaData metaData = new MPSReader().process(new File(remainingArgs[0]), new FileWriter(outputFile));

        TableCreator tableCreator = new TableCreator();
        tableCreator.create(metaData);

        Job job = new Job(getConf(), "MpsLoader");
        job.setJarByClass(getClass());

        job.setMapperClass(FirstPartAsKeyMapper.class);
        job.setReducerClass(RowCreatorReducer.class);

        job.setOutputFormatClass(TableOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(outputFile.getAbsolutePath()));

        boolean success = job.waitForCompletion(true);

        return success ? 0 : 1;
    }

}
