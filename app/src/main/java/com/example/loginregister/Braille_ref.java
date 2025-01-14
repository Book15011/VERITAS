package com.example.loginregister;

import java.util.HashMap;

public class Braille_ref {
    private static final HashMap<Character, String> brailleMap = new HashMap<>();

    static {
        brailleMap.put('A', "100000");
        brailleMap.put('a', "100000");

        brailleMap.put('B', "101000");
        brailleMap.put('b', "101000");

        brailleMap.put('C', "110000");
        brailleMap.put('c', "110000");

        brailleMap.put('D', "110100");
        brailleMap.put('d', "110100");

        brailleMap.put('E', "100100");
        brailleMap.put('e', "100100");

        brailleMap.put('F', "111000");
        brailleMap.put('f', "111000");

        brailleMap.put('G', "111100");
        brailleMap.put('g', "111100");

        brailleMap.put('H', "101100");
        brailleMap.put('h', "101100");

        brailleMap.put('I', "010100");
        brailleMap.put('i', "010100");

        brailleMap.put('J', "011100");
        brailleMap.put('j', "011100");

        brailleMap.put('K', "100010");
        brailleMap.put('k', "100010");

        brailleMap.put('L', "101010");
        brailleMap.put('l', "101010");

        brailleMap.put('M', "110010");
        brailleMap.put('m', "110010");

        brailleMap.put('N', "110110");
        brailleMap.put('n', "110110");

        brailleMap.put('O', "100110");
        brailleMap.put('o', "100110");

        brailleMap.put('P', "111010");
        brailleMap.put('p', "111010");

        brailleMap.put('Q', "111110");
        brailleMap.put('q', "111110");

        brailleMap.put('R', "101110");
        brailleMap.put('r', "101110");

        brailleMap.put('S', "011010");
        brailleMap.put('s', "011010");

        brailleMap.put('T', "011110");
        brailleMap.put('t', "011110");

        brailleMap.put('U', "100011");
        brailleMap.put('u', "100011");

        brailleMap.put('V', "101011");
        brailleMap.put('v', "101011");

        brailleMap.put('W', "011101");
        brailleMap.put('w', "011101");

        brailleMap.put('X', "110011");
        brailleMap.put('x', "110011");

        brailleMap.put('Y', "110111");
        brailleMap.put('y', "110111");

        brailleMap.put('Z', "100111");
        brailleMap.put('z', "100111");

        brailleMap.put(' ', "000000"); // Space character
    }

    public static String getBrailleRepresentation(char character) {
        return brailleMap.get(Character.toLowerCase(character));
    }

    // Add this new public method to get the brailleMap
    public static HashMap<Character, String> getBrailleMap() {
        return brailleMap;
    }
}