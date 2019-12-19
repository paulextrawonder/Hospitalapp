package com.example.eeganalysistoolkit.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

abstract class ParseUtils
{
    public static String[] readBulkASCIIFromStream(InputStream is, int size, int length) throws IOException
    {
        String[] result = new String[length];
        for (int i = 0; i < length; i++)
        {
            result[i] = readASCIIFromStream(is, size);
        }
        return result;
    }

    public static Double[] readBulkDoubleFromStream(InputStream is, int size, int length) throws IOException
    {
        Double[] result = new Double[length];
        for (int i = 0; i < length; i++)
            result[i] = Double.parseDouble(readASCIIFromStream(is, size).trim());
        return result;
    }

    public static Integer[] readBulkIntFromStream(InputStream is, int size, int length) throws IOException
    {
        Integer[] result = new Integer[length];
        for (int i = 0; i < length; i++)
            result[i] = Integer.parseInt(readASCIIFromStream(is, size).trim());
        return result;
    }

    public static String readASCIIFromStream(InputStream is, int size) throws IOException
    {
        int len;
        byte[] data = new byte[size];
        len = is.read(data);
        if (len != data.length)
            throw new EDFParserException();
        return new String(data, EDFConstants.CHARSET);
    }

    public static <T> T[] removeElement(T[] array, int i)
    {
        if (i < 0)
            return array;
        if (i == 0)
            return Arrays.copyOfRange(array, 1, array.length);
        T[] result = Arrays.copyOfRange(array, 0, array.length - 1);
        System.arraycopy(array, i + 1, result, i + 1 - 1, array.length - (i + 1));
        return result;
    }
}