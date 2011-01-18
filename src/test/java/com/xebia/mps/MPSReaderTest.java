package com.xebia.mps;

import junit.framework.Assert;
import org.antlr.runtime.RecognitionException;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.StringWriter;
import java.net.URISyntaxException;

import static junit.framework.Assert.assertEquals;

public class MPSReaderTest {

    @Test
    public void shouldProcessTestProb() throws Exception {
        MPSReader mpsReader = new MPSReader();
        File in = file("testprob.mps");
        StringWriter out = new StringWriter();
        MPSMetaData metaData = mpsReader.process(in, out);
        assertEquals("Testprob", metaData.getName());
        assertEquals("COST", metaData.getTarget());
        System.out.println(out);
    }

    @Test
    public void shouldProcessAdLittle() throws Exception {
        MPSReader mpsReader = new MPSReader();
        File in = file("adlittle.mps");
        StringWriter out = new StringWriter();
        MPSMetaData metaData = mpsReader.process(in, out);
        System.out.println(out);
    }

    @Test
    public void shouldProcessAfiro() throws Exception {
        MPSReader mpsReader = new MPSReader();
        File in = file("afiro.mps");
        StringWriter out = new StringWriter();
        MPSMetaData metaData = mpsReader.process(in, out);
        System.out.println(out);
    }

    @Test(expected = RecognitionException.class)
    public void shouldComplainWhenNoName() throws Exception {
        new MPSReader().process(file("error1.mps"), new StringWriter());
    }

    private File file(String filename) throws URISyntaxException {
        return new File(MPSReaderTest.class.getResource("/" + filename).toURI());
    }
}
