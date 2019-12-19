package com.example.eeganalysistoolkit.model;

import java.io.IOException;

class EDFParserException extends IOException {
    private static final long serialVersionUID = 3807109927368496625L;

    public EDFParserException()
    {
        this("File format not according to EDF/EDF+ specification.", null);
    }

    public EDFParserException(Throwable th)
    {
        this("File format not according to EDF/EDF+ specification.", th);
    }

    public EDFParserException(String message, Throwable th)
    {
        super(message, th);
    }
}
