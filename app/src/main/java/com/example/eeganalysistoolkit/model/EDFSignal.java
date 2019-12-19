package com.example.eeganalysistoolkit.model;

public class EDFSignal {

    Double[] unitsInDigit;
    short[][] digitalValues;
    double[][] valuesInUnits;

    public Double[] getUnitsInDigit()
    {
        return unitsInDigit;
    }

    public short[][] getDigitalValues()
    {
        return digitalValues;
    }

    public double[][] getValuesInUnits()
    {
        return valuesInUnits;
    }
}