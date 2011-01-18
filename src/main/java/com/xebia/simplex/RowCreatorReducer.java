package com.xebia.simplex;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class RowCreatorReducer extends Reducer<Text, Text, Text, Put> {
}
