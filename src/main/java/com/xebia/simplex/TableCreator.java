package com.xebia.simplex;

import com.xebia.mps.MPSMetaData;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class TableCreator {
    void create(MPSMetaData metaData) throws IOException {
        HTableDescriptor tableDescriptor = createTableDescriptorFrom(metaData);
        createTable(tableDescriptor);
    }

    void createTable(HTableDescriptor tableDescriptor) throws IOException {
        HBaseAdmin admin = new HBaseAdmin(new HBaseConfiguration());
        admin.createTable(tableDescriptor);
    }

    HTableDescriptor createTableDescriptorFrom(MPSMetaData metaData) {
        HTableDescriptor tableDescriptor = new HTableDescriptor();
        tableDescriptor.setName(Bytes.toBytes(metaData.getName()));
        tableDescriptor.addFamily(new HColumnDescriptor("_RowId"));
        tableDescriptor.addFamily(new HColumnDescriptor("_PivotColumn"));
        for (String column : metaData.getColumns()) {
            tableDescriptor.addFamily(new HColumnDescriptor(column));
        }
        tableDescriptor.addFamily(new HColumnDescriptor(metaData.getTarget()));
        return tableDescriptor;
    }
}
