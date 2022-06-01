package se.kau.cs.sy.services.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

public class GeneralUtils {

    public static byte[] toByteArray(Collection<Byte> c) {
        if (c == null) { return null; }
        byte[] array = new byte[c.size()];
        int i = 0;
        for (Byte b : c) {
            array[i++] = b;
        }
        return array;
    }

    public static byte[] readAllBytes(InputStream is) {
        if (is == null) { return null; }
        List<Byte> bytes = new ArrayList<>();
        int i;

        try {
            while ((i = is.read()) != -1) {
                bytes.add((byte)i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return toByteArray(bytes);
    }
}
