package com.xebia.simplex;

import com.xebia.mps.MPSMetaData;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TableCreatorTest {

    @Test
    public void shouldTransformMetaDataToTableDescriptor() throws IOException {
        HTableDescriptor tableDescriptor = new TableCreator().createTableDescriptorFrom(metaData());
        assertEquals("Blaat", Bytes.toString(tableDescriptor.getName()));
        assertEquals(7, tableDescriptor.getColumnFamilies().length);
    }

    @Ignore("Keeps looping when master not up.")
    @Test
    public void shouldCreateTable() throws IOException {
        try {
            HBaseAdmin admin = new HBaseAdmin(new HBaseConfiguration());
        } catch (MasterNotRunningException e) {
            // Not running
            return;
        }
        new TableCreator().create(metaData());
    }

    private MPSMetaData metaData() {
        MPSMetaData metaData = new MPSMetaData();
        metaData.setName("Blaat");
        metaData.getColumns().add("Col1");
        metaData.getColumns().add("Col2");
        metaData.getColumns().add("Col3");
        metaData.getColumns().add("Col4");
        metaData.registerRow('N', "Target");
        return metaData;
    }

}
