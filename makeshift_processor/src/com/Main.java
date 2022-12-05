package com;

import com.assembly.Assembler;

import java.util.Scanner;

import com.processor.*;
import com.rom.ReadOnlyMemory;

public class Main {

    // hexadecimal code lines:


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //int number_0 = scanner.nextInt();
        //int number_1 = scanner.nextInt();

        //Hex hex_0 = Encoder.Dec2Hex(number_0);
        //Hex hex_1 = Encoder.Dec2Hex(number_1);

        ProcessorX86 processor = new ProcessorX86();
        //processor.GetInfo();

        //processor.Register_A.Set(0, Encoder.Dec2Hex(number_0));
        //processor.Register_A.Set(1, Encoder.Dec2Hex(number_1));
        //processor.GetInfo();

        //processor.ExecuteCommand(new MicroCommand("AB 00 11"));
        //processor.GetInfo();

        //processor.ExecuteCommand(new MicroCommand("AD 0A"));
        //processor.GetInfo();

        //processor.ExecuteCommand(new MicroCommand("AE 07"));
        //processor.GetInfo();

//        String command = "";
//
//        while (!(command.equals("Q") || command.equals("q"))) {
//            command = scanner.nextLine();
//
//            if (!(command.equals("Q") || command.equals("q"))) {
//                processor.ExecuteCommand(new MicroCommand(command));
//                processor.GetInfo();
//            }
//            else {
//                System.out.println("Quitting process...");
//            }
//        }

//        System.out.println("-------------");
        String assembler = Assembler.ReadFromFile("txt_files/assembler.txt");
//        System.out.println(assembler);
//        System.out.println("-------------");
//        System.out.println();
//
//        System.out.println("-------------");
        String[] assembler_hex = Assembler.Assembler2Hex(assembler);
//        for (int i = 0; i < assembler_hex.length; i++) {
//            System.out.println("/" + assembler_hex[i] + "/");
//        }
//        System.out.println("-------------");

        String commands[] = {
                "AD 0A AA 00 02",
                "AD 0A AA 00 01",
                "AD 0B AA 00 03",
                "AD 0A 11 0B 00",
                "01 11 0C AA 00 04",
                "01 11 0D 11 0C",
                "01 AA 00 00 11 0C",
                "AE 0D DD 11 11",
                "AE 0D AA 00 05",
                "AE 0C 11 0D",
                "AA DD 00 A0",
                "01 11 0A DD 00 AA",
                "01 11 0B AA 00 07",
                "AA 11 0B",
                "AA AA 00 07",
                "AB 11 0B",
                "AB AA 00 07",
                "AB DD 00 A9",
                "01 AA 00 0A 11 0D",
                "A0 0B 11 0A",
                "0A 0B AA 00 0A",
                "A0 0B DD 00 01",
                "EE 11 0C",
                "AD 0C DD 00 04",
                "12 11 0C",
                "01 11 0A DD 80 08",
                "01 11 0B DD 80 08",
                "DD 11 0B",
                "DD AA 00 01",
                "DE 11 0B",
                "DE AA 00 01",
                "10",
                "DD AA 00 0F",
                "DE 11 0C",
                "11",
                "FF"
        };

        String[] mem_com = {
                "FA",
                "02 00 01",
                "02 00 11",
                "02 00 AA",
                "02 00 55",
                "02 FF FF",
                "02 11 11",
                "02 01 01",
                "02 00 10",
                "FF"
        };

        String memory[] = {
                "00 01",
                "00 11",
                "00 AA",
                "00 55",
                "FF FF",
                "11 11",
                "01 01",
                "00 10"
        };

        System.out.println();

        //processor.ROM = new ReadOnlyMemory(mem_com, mem_com.length, 8);

        //processor.Memory = new ReadOnlyMemory(memory, 20, 2);
        processor.ROM = new ReadOnlyMemory(assembler_hex, assembler_hex.length, 8);

        //processor.ROM = new ReadOnlyMemory(commands, 100, 8);

        processor.ExecuteROM();

        //processor.Memory.GetInfo();

//        processor.ExecuteCommand(new MicroCommand("AD 0A AA 00 02"));
//        processor.ExecuteCommand(new MicroCommand("AD 0A AA 00 01"));
//        processor.GetInfo();
//
//        processor.ExecuteCommand(new MicroCommand("AD 0B AA 00 03"));
//        processor.ExecuteCommand(new MicroCommand("AD 0A 11 0B 00"));
//        processor.GetInfo();

    }

}
