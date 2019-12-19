package com.example.eeganalysistoolkit.model;

import java.util.List;

public class EDFParserResult {
    EDFHeader header;
    EDFSignal signal;
    List<EDFAnnotation> annotations;

    public EDFHeader getHeader()
    {
        return header;
    }

    public EDFSignal getSignal()
    {
        return signal;
    }

    public List<EDFAnnotation> getAnnotations()
    {
        return annotations;
    }
}
