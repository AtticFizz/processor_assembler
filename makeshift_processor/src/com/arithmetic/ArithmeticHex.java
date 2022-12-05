package com.arithmetic;

import com.code.Hex;
import com.encoder.Encoder;

public class ArithmeticHex {

    public static Hex Add(Hex number_0, Hex number_1) {
        return Encoder.Dec2Hex(Encoder.Hex2Dec(number_0) + Encoder.Hex2Dec(number_1));
    }

    public static Hex Subtract(Hex number_0, Hex number_1) {
        return Encoder.Dec2Hex(Encoder.Hex2Dec(number_0) - Encoder.Hex2Dec(number_1));
    }

    public static Hex Multiply(Hex number_0, Hex number_1) {
        return Encoder.Dec2Hex(Encoder.Hex2Dec(number_0) * Encoder.Hex2Dec(number_1));
    }

    public static Hex Divide(Hex number_0, Hex number_1) {
        return Encoder.Dec2Hex((Encoder.Hex2Dec(number_0) / Encoder.Hex2Dec(number_1)));
    }

    public static Hex Remainder(Hex number_0, Hex number_1) {
        return Encoder.Dec2Hex((Encoder.Hex2Dec(number_0) % Encoder.Hex2Dec(number_1)));
    }


}
