package domain;

import java.util.stream.IntStream;

public class RC4Cipher {
    public int[] cipher(int[] plainText, int[] key) {
        if (key.length < 5 || key.length> 32) {
            return null;
        }
        int[] ascciCharacters = rc4(key);
        int[] cipherTex = new int[plainText.length];
        int[] prga = generateOutputPRGA(ascciCharacters, plainText.length);
        for (int i = 0; i < plainText.length; i++) {
            cipherTex[i] = plainText[i] ^ prga[i];
        }
        return cipherTex;
    }

    public static int[] buildKey(String text) {
        int[] characters = new int[text.length()];
        for (int i = 0; i < text.length();  i++) {
            characters[i] = text.charAt(i);
        }
        return characters;
    }

    public static int[] textToBin(String text) {
        int[] bytes = new int[text.length()];
        for (int i = 0; i < text.length(); i++) {
            bytes[i] = text.charAt(i);
        }
        return bytes;
    }

    public static String binToText(int[] bits) {
        String text = "";
        for (int i = 0; i < bits.length; i++) {
            text += (char)bits[i];
        }
        return text;
    }

    private int[] rc4(int[] key) {
        int[] S = IntStream.range(0, 256).toArray();
        int j = 0;
        int keylength = key.length;
        for (int i = 0; i < 256; i++) {
            j = (j + S[i] + key[i % keylength]) % 256;
            int temp = S[j];
            S[j] = S[i];
            S[i] = temp;
        }
        return S;
    }

    public int[] generateOutputPRGA(int [] S, int length){

        int i=0;
        int j=0;
        int [] output = new int [length];

        for(int car=0; car<length; car++){
            i = (i + 1) % 256;
            j = (j + S[i]) % 256;

            int swap_var = S[i];
            S[i] = S[j];
            S[j] = swap_var;

            output[car] = S[(S[i] + S[j]) % 256];
        }
        return output;
    }
}
