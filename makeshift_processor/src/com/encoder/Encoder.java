package com.encoder;

import com.code.Hex;

public class Encoder {

    public static Hex Dec2Hex(int decimal) {
        if (decimal == 0) {
            return new Hex("00");
        }

        String hexadecimal = "";

        while (decimal != 0) {
            int remainder = decimal % 16;
            decimal /= 16;

            String symbol = "?";
            if (remainder < 10) {
                symbol = Integer.toString(remainder);
            } else if (remainder <= 15) {
                if (remainder == 10) {
                    symbol = "A";
                } else if (remainder == 11) {
                    symbol = "B";
                } else if (remainder == 12) {
                    symbol = "C";
                } else if (remainder == 13) {
                    symbol = "D";
                } else if (remainder == 14) {
                    symbol = "E";
                } else {
                    symbol = "F";
                }
            }

            hexadecimal = symbol + hexadecimal;
        }

        if (hexadecimal.length() % 2 != 0) {
            hexadecimal = "0" + hexadecimal;
        }

        return new Hex(hexadecimal);
    }

    public static int Hex2Dec(Hex hex) {
        int dec = 0;

        for (int i = 0; i < hex.Get().length(); i++) {
            dec += Math.pow(16, i) * hex2dex(hex.Get().charAt(hex.Get().length() - 1 - i));
        }
        //System.out.println(dec);

        return dec;
    }

    private static int hex2dex(char symbol) {
        if (Character.isDigit(symbol)) {
            return Character.getNumericValue(symbol);
        }
        else if (symbol == 'A') {
            return 10;
        }
        else if (symbol == 'B') {
            return 11;
        }
        else if (symbol == 'C') {
            return 12;
        }
        else if (symbol == 'D') {
            return 13;
        }
        else if (symbol == 'E') {
            return 14;
        }
        else if (symbol == 'F') {
            return 15;
        }
        return 0;
    }

    public static Hex[] Str2HexArray(String line) {
        String[] array = line.split(" ");
        Hex[] hex_array = new Hex[array.length];

        for (int i = 0; i < array.length; i++) {
            hex_array[i] = new Hex(array[i]);
        }

        return hex_array;
    }

    public static String Str2Str_Gap(String value) {
        String new_value = "";

        int gap = 0;
        for (int i = 0; i < value.length(); i++) {
            new_value += value.charAt(i);
            gap++;
            if (gap == 2) {
                new_value += " ";
                gap = 0;
            }
        }

        return new_value;
    }

    public static String Byte2Word(String value) {
        return "00 " + value;
    }

    public static String HexArray2Str(Hex[] array) {
        String line = "";

        for (int i = 0; i < array.length; i++) {
            line += array[i].Get() + " ";
        }

        return line;
    }

    public static String Hex2Str_Word(Hex hex0, Hex hex1) {
        return hex0.Get() + " " + hex1.Get();
    }

    public static String HexArray2Str_NoGap(Hex[] array) {
        String line = "";

        for (int i = 0; i < array.length; i++) {
            line += array[i].Get();
        }

        return line;
    }

    public static String Dec2Str_Hex_Word(int decimal) {
        Hex hex = Dec2Hex(decimal);
        Hex[] hex_array = Str2HexArray(hex.Get());

        if (hex_array.length == 1) {
            return hex.Get();
        }

        return Str2Str_Gap(hex.Get());
    }


}
