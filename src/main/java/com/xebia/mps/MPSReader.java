package com.xebia.mps;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

/**
 * Reads an MPS file.
 */
public class MPSReader {

    protected final static Log LOG = LogFactory.getLog(MPSReader.class);
    public MPSMetaData process(File in, Writer out) throws IOException, RecognitionException {
        MPSLexer lexer = new MPSLexer(new ANTLRInputStream(new FileInputStream(in)));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        MPSParser parser = new MPSParser(tokenStream);
        MPSMetaData metaData = new MPSMetaData();
        metaData.setDataSink(out);
        parser.setMetaData(metaData);
        parser.parse();
        return metaData;
    }

}
