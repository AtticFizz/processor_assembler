package com.rom;

import com.code.Hex;
import com.encoder.Encoder;

public class ReadOnlyMemory {

    private Hex[][] memory;

    public ReadOnlyMemory() {
        memory = new Hex[10][2];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 2; j++) {
                memory[i][j] = new Hex();
            }
        }
    }
    public ReadOnlyMemory(int size) {
        memory = new Hex[size][2];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < 2; j++) {
                memory[i][j] = new Hex();
            }
        }
    }
    public ReadOnlyMemory(int size, int length) {
        memory = new Hex[size][length];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < length; j++) {
                memory[i][j] = new Hex();
            }
        }
    }
    public ReadOnlyMemory(String[] values, int size, int length) {
        memory = new Hex[size][length];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < length; j++) {
                memory[i][j] = new Hex();
            }
        }

        Set(values);
    }

    public Hex[] GetAt(int address) {
        return memory[address];
    }

    public void Set(int address, int position, Hex value) {
        memory[address][position] = value;
    }
    public void Set(int address, String value) {
        Hex[] value_hex = Encoder.Str2HexArray(value);

        for (int i = 0; i < value_hex.length; i++) {
            memory[address][i] = value_hex[i];
        }
    }
    public void Set(String[] values) {
        for (int i = 0; i < values.length; i++) {
            Set(i, values[i]);
        }
    }

    public void GetInfo() {
        for (int i = 0; i < memory.length; i++) {
            for (int j = 0; j < memory[i].length; j++) {
                System.out.print(memory[i][j].Get() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

}
