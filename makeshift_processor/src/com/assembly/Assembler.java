package com.assembly;

import com.code.Hex;
import com.encoder.Encoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Scanner;

public class Assembler {

    private static Dictionary<String, Integer> Variables = new Hashtable<>();
    private static int Address = 0;

    public static String[] Assembler2Hex(String code) {
        String adjusted = code.replaceAll("(?m)^[ \t]*\r?\n", "");
        String[] code_lines = adjusted.split("\\r?\\n");
        String[] code_hex = new String[code_lines.length];

        for (int i = 0; i < code_lines.length;i++) {
            String adj = code_lines[i].replaceAll("    ", "");
            String[] elements = adj.split(" ");

            if (elements[0].toLowerCase().equals("add")) {
                code_hex[i] = Add(elements);
            }
            else if (elements[0].toLowerCase().equals("sub")) {
                code_hex[i] = Subtract(elements);
            }
            else if (elements[0].toLowerCase().equals("mul")) {
                code_hex[i] = Multiply(elements);
            }
            else if (elements[0].toLowerCase().equals("div")) {
                code_hex[i] = Divide(elements);
            }
            else if (elements[0].toLowerCase().equals("inc")) {
                String[] new_line = new String[] { elements[0], elements[1] , "1"};
                code_hex[i] = Add(new_line);
            }
            else if (elements[0].toLowerCase().equals("dec")) {
                String[] new_line = new String[] { elements[0], elements[1] , "1"};
                code_hex[i] = Subtract(new_line);
            }
            else if (elements[0].toLowerCase().equals("shr")) {
                code_hex[i] = ShiftRight(elements);
            }
            else if (elements[0].toLowerCase().equals("shl")) {
                code_hex[i] = ShiftLeft(elements);
            }
            else if (elements[0].toLowerCase().equals("int")) {
                code_hex[i] = SetVariable_Decimal(elements);
            }
            else if (elements[0].toLowerCase().equals("move")) {
                code_hex[i] = Move(elements);
            }
            else if (elements[0].toLowerCase().equals("addr")) {
                code_hex[i] = GetAddress(elements);
            }
            else if (elements[0].toLowerCase().equals("jump")) {
                code_hex[i] = Jump(elements);
            }
            else if (elements[0].toLowerCase().equals("process:")) {
                code_hex[i] = "00";
            }
            else if (elements[0].toLowerCase().equals("memory:")) {
                code_hex[i] = "FA";
            }
            else if (elements[0].toLowerCase().equals("loop:")) {
                code_hex[i] = "10";
            }
            else if (elements[0].toLowerCase().equals("end")) {
                if (elements[1].toLowerCase().equals("process")) {
                    code_hex[i] = "FF";
                }
                else if (elements[1].toLowerCase().equals("loop")) {
                    code_hex[i] = "11";
                }
                else if (elements[1].toLowerCase().equals("memory")) {
                    code_hex[i] = "00";
                }
            }
        }

        return code_hex;
    }

    public static String ReadFromFile(String file) {
        String data = "";

        try {
            File myObj = new File(file);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data += myReader.nextLine() + '\n';
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return data;
    }

    private static String Dec2Hex_One_Op(int dec) {
        return Encoder.Dec2Str_Hex_Word(dec);
    }

    private static String Dec2Hex_Two_Op(int dec0, int dec1) {
        String op0 = Encoder.Dec2Str_Hex_Word(dec0);
        String op1 = Encoder.Dec2Str_Hex_Word(dec1);

        return op0 + " " + op1;
    }

    private static String GetRegister(String register) {
        if (register.toLowerCase().equals("ax")) {
            return "0A";
        }
        else if (register.toLowerCase().equals("bx")) {
            return "0B";
        }
        else if (register.toLowerCase().equals("cx")) {
            return "0C";
        }
        else if (register.toLowerCase().equals("dx")) {
            return "0D";
        }
        else if (register.toLowerCase().equals("lo")) {
            return "10";
        }
        else {
            return "NOT_REG";
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private static String AddressMode_Value(String value) {
        String register = GetRegister(value);

        if (!register.equals("NOT_REG")) {
            return "11 " + register;
        }
        else {
            if (isNumeric(value)) {
                int number = Integer.parseInt(value);
                return "DD " + Encoder.Dec2Hex(number / 256).Get() + " " + Encoder.Dec2Hex(number % 256).Get();
            }
            else {
                int number = Variables.get(value);
                return "AA " + Encoder.Dec2Hex(number / 256).Get() + " " + Encoder.Dec2Hex(number % 256).Get();
            }
        }

    }

    private static String GetValueAt(int address) {
        int number = Variables.get(address);
        return Encoder.Dec2Hex(number / 256).Get() + " " + Encoder.Dec2Hex(number % 256).Get();
    }

    private static String Dec2Str(int address) {
        return Encoder.Dec2Hex(address / 256).Get() + " " + Encoder.Dec2Hex(address % 256).Get();
    }

    private static String Add(String[] elements) {
        return "AD " + GetRegister(elements[1]) + " " + AddressMode_Value(elements[2]);
    }

    private static String Subtract(String[] elements) {
        return "AE " + GetRegister(elements[1]) + " " + AddressMode_Value(elements[2]);
    }

    private static String Multiply(String[] elements) {
        return "AA " + AddressMode_Value(elements[1]);
    }

    private static String Divide(String[] elements) {
        return "AB " + AddressMode_Value(elements[1]);
    }

    private static String ShiftRight(String[] elements) {
        return "0A " + GetRegister(elements[1]) + " " + AddressMode_Value(elements[2]);
    }

    private static String ShiftLeft(String[] elements) {
        return "A0 " + GetRegister(elements[1]) + " " + AddressMode_Value(elements[2]);
    }

    private static String SetVariable_Decimal(String[] elements) {
        int number = Integer.parseInt(elements[2]);
        Variables.put(elements[1], Address);
        Address++;
        return "02 " + Encoder.Dec2Hex(number / 256).Get() + " " + Encoder.Dec2Hex(number % 256).Get();
    }

    private static String Move(String[] elements) {
        String register = GetRegister(elements[1]);

        if (!register.equals("NOT_REG")) {
            return "01 11 " + register + " " + AddressMode_Value(elements[2]);
        }
        else {
            int number = Variables.get(elements[1]);
            return "01 AA " + Encoder.Dec2Hex(number / 256).Get() + " " + Encoder.Dec2Hex(number % 256).Get() + " 11 " + GetRegister(elements[2]);
        }
    }

    private static String GetAddress(String[] elements) {
        String register = GetRegister(elements[1]);

        if (!register.equals("NOT_REG")) {
            int variable_address = Variables.get(elements[2]);
            return "DA 11 " + register + " " + Dec2Str(variable_address);
        }
        else {
            int var_write = Variables.get(elements[1]);
            int var_get = Variables.get(elements[2]);
            return "DA AA " + Dec2Str(var_write) + " " + Dec2Str(var_get);
        }
    }

    private static String Jump(String[] elements) {
        String register = GetRegister(elements[1]);

        if (!register.equals("NOT_REG")) {
            return "12 11 " + register;
        }
        else {
            int address = Variables.get(elements[1]);
            return "12 AA " + Dec2Str(address);
        }
    }



}
