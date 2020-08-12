package domain;

import presentation.LogViewer;

import java.util.Arrays;

public class PixelsLeastSignificantBit {
    private int[] makeByteFieldForMessageLength(int length) {
        int[] bits = new int[8];
        for (int i = 0; i < 8; i++) {
            bits[7 - i] = length >> i & 1;
        }
        return  bits;
    }
    private int lsb(int pixel, int bit) {
        if ((pixel & 1) == bit) return pixel;
        return pixel ^ 1;
    }
    private void insertLeastSignificantBitRange(int[] dataSource, int[] bits, int index) {
        for (int i = 0; i < 8; i++) {
            int lsb = lsb(dataSource[index + i], bits[i]);
            LogViewer.logger.log(String.format("LSB index: [%d] | old: %d, new: %d", lsb & 1, dataSource[index + i], lsb));
            dataSource[index + i] = lsb;
        }
    }
    public int[] generateLeastSignificantBitArrayFromString(int[] data, String message) {
        LogViewer.logger.log(String.format("Mensaje: \"%s\"", message));
        int[] lengthField = makeByteFieldForMessageLength(message.length());
        LogViewer.logger.log("@@@ Length: " + message.length() + ", binary format: " + Arrays.toString(lengthField));
        insertLeastSignificantBitRange(data, lengthField, 0);
        for (int i = 0; i < message.length(); i++) {
            int[] bitsCharacter = makeByteFieldForMessageLength(message.charAt(i));
            int indexToInsert = i * 8 + 8;
            LogViewer.logger.log(
                    String.format(
                            "Caracter: \"%s\", Insert: %s - [%d]",
                            message.charAt(i),
                            Arrays.toString(bitsCharacter),
                            indexToInsert
                    )
            );
            insertLeastSignificantBitRange(data, bitsCharacter, indexToInsert);
        }
        return data;
    }
    public String generateStringFromLeastSignificantBitArray(int[] bits) {
        int lengthField = 0;
        for (int i = 0; i < 8; i++) {
            LogViewer.logger.log(String.format(">>> Leyendo bit: [%d]", bits[i] & 1));
            lengthField += (bits[7 - i] & 1) << i;
        }
        LogViewer.logger.log(String.format(">>> Longitud leida: [%d]", lengthField));
        StringBuilder message = new StringBuilder();
        for (int i = 8; i < lengthField * 8 + 8; i+=8) {
            char character = '\0';
            for (int j = 0; j < 8; j++) {
                int index = 7 + i - j;
                LogViewer.logger.log(String.format(">>> Leyendo posicion: [%d] -> valor: %d", index, (bits[index] & 1) << j));
                character += (bits[index] & 1) << j;
            }
            message.append((char) character);
        }
        return message.toString();
    }
}
