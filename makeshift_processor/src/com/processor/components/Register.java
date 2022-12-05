package com.processor.components;

import com.code.Hex;
import com.encoder.Encoder;

public class Register {

    private int number;

    public Register() {
        this.number = 0;
    }
    public Register(Hex number) {
        this.number = Encoder.Hex2Dec(number);
    }

    public String GetInfo() {
        return Encoder.Dec2Hex(number / 256).Get() + " " + Encoder.Dec2Hex(number % 256).Get();
    }
    public String GetInfo(int index) {
        // High byte
        if (index == 1) {
            return Encoder.Dec2Hex(number / 256).Get();
        }
        // Low byte
        else if (index == 0) {
            return Encoder.Dec2Hex(number % 256).Get();
        }
        else {
            System.out.println("Register with index " + index + " doesn't exist. Index should be 1 (high) or 0 (low).");
        }

       return new Hex("00").Get();
    }

    public Hex Get() {
        return Encoder.Dec2Hex(number);
    }
    public Hex Get(int index) {
        // High byte
        if (index == 1) {
            return Encoder.Dec2Hex(number / 256);
        }
        // Low byte
        else if (index == 0) {
            return Encoder.Dec2Hex(number % 256);
        }
        else {
            System.out.println("Register with index " + index + " doesn't exist. Index should be 1 (high) or 0 (low).");
        }

        return new Hex("00");
    }

    public void Set(Hex number) {
        this.number = Encoder.Hex2Dec(number);
    }
    public void Set(int index, Hex number) {
        // High byte
        if (index == 1) {
            this.number = Encoder.Hex2Dec(number) / 256;
        }
        // Low byte
        else if (index == 0) {
            this.number = Encoder.Hex2Dec(number) % 256;
        }
        else {
            System.out.println("Register with index " + index + " doesn't exist. Index should be 1 (high) or 0 (low).");
        }
    }

    public void Increase() {
        number++;
    }
    public void Decrease() {
        number--;
    }

    public void ShiftLeft(int times) {
        number = number << times;
    }

    public void ShiftRight(int times) {
        number = number >> times;
    }


}
