package com.processor;

import com.arithmetic.ArithmeticHex;
import com.code.Hex;
import com.encoder.Encoder;
import com.microcommand.MicroCommand;
import com.processor.components.Register;
import com.rom.ReadOnlyMemory;

import java.util.Dictionary;

public class ProcessorX86 {

    public Register Register_A = new Register();
    public Register Register_B = new Register();
    public Register Register_C = new Register();
    public Register Register_D = new Register();

    public Register Register_Lo = new Register();

    public ReadOnlyMemory ROM = new ReadOnlyMemory();
    public int CurrentCodeAddress = 0;

    public ReadOnlyMemory Memory = new ReadOnlyMemory(5, 2);
    public int CurrentMemoryAddress = 0;

    public Dictionary<String, Integer> Stack;

    public ProcessorX86() {}

    private void Add(Register reg, Hex op) {
        reg.Set(ArithmeticHex.Add(reg.Get(), op));
    }

    private void Subtract(Register reg, Hex op) {
        reg.Set(ArithmeticHex.Subtract(reg.Get(), op));
    }

    private void Multiply(Hex op) {
        Register_A.Set(ArithmeticHex.Multiply(Register_A.Get(), op));
    }

    private void Divide(Hex op) {
        Register_D.Set(ArithmeticHex.Remainder(Register_A.Get(), op));
        Register_A.Set(ArithmeticHex.Divide(Register_A.Get(), op));
    }

    private Register GetRegister(Hex index) {
        if (index.Get().equals("0A")) {
            return Register_A;
        }
        else if (index.Get().equals("0B")) {
            return Register_B;
        }
        else if (index.Get().equals("0C")) {
            return Register_C;
        }
        else if (index.Get().equals("0D")) {
            return Register_D;
        }
        else {
            System.out.println("No register with index " + index.Get() + ". Should be [0A;0B].");
        }

        return Register_A;
    }

    public void GetInfo() {
        System.out.println("Ax: " + Register_A.GetInfo());
        System.out.println("Bx: " + Register_B.GetInfo());
        System.out.println("Cx: " + Register_C.GetInfo());
        System.out.println("Dx: " + Register_D.GetInfo());
        System.out.println();
    }

    private Hex GetValue_AddressMode(Hex place, Hex addr0, Hex addr1) {
        if (place.Equals("11")) {
            return new Hex(GetRegister(addr0).Get().Get());
        }
        else if (place.Equals("AA")) {
            int index = Encoder.Hex2Dec(addr0.Merge(addr1));
            return new Hex(Memory.GetAt(index)[0].Merge(Memory.GetAt(index)[1]).Get());
        }
        else if (place.Equals("DD")) {
            return new Hex(addr0.Merge(addr1).Get());
        }

        System.out.println(place.Get() + " operator undefined.");

        return new Hex("00");
    }

    private void Add(MicroCommand micro_command) {
        Hex[] op = Encoder.Str2HexArray(micro_command.Get());

        Register register = GetRegister(op[1]);
        Hex value = GetValue_AddressMode(op[2], op[3], op[4]);

        Add(register, value);
    }

    private void Subtract(MicroCommand micro_command) {
        Hex[] op = Encoder.Str2HexArray(micro_command.Get());

        Register register = GetRegister(op[1]);
        Hex value = GetValue_AddressMode(op[2], op[3], op[4]);

        Subtract(register, value);
    }

    private void Multiply(MicroCommand micro_command) {
        Hex[] op = Encoder.Str2HexArray(micro_command.Get());
        Hex value = GetValue_AddressMode(op[1], op[2], op[3]);

        Multiply(value);
    }

    private void Divide(MicroCommand micro_command) {
        Hex[] op = Encoder.Str2HexArray(micro_command.Get());
        Hex value = GetValue_AddressMode(op[1], op[2], op[3]);

        Divide(value);
    }

    private void ShiftRigth(MicroCommand micro_command) {
        Hex[] op = Encoder.Str2HexArray(micro_command.Get());

        Register register = GetRegister(op[1]);
        int times = Encoder.Hex2Dec(GetValue_AddressMode(op[2], op[3], op[4]));

        if (times <= 32) {
            register.ShiftRight(times);
        }
        else {
            System.out.println("Can't shift " + times + " times. Maximum is 32.");
        }
    }

    private void ShiftLeft(MicroCommand micro_command) {
        Hex[] op = Encoder.Str2HexArray(micro_command.Get());

        Register register = GetRegister(op[1]);
        int times = Encoder.Hex2Dec(GetValue_AddressMode(op[2], op[3], op[4]));

        if (times <= 32) {
            register.ShiftLeft(times);
        }
        else {
            System.out.println("Can't shift " + times + " times. Maximum is 32.");
        }
    }

    private void Move(MicroCommand micro_command) {
        Hex[] op = Encoder.Str2HexArray(micro_command.Get());

        if (op[1].Equals("11")) {
            Register register = GetRegister(op[2]);
            Hex value = GetValue_AddressMode(op[3], op[4], op[5]);

            register.Set(value);
        }
        else if (op[1].Equals("AA")) {
            Hex value = GetValue_AddressMode(op[4], op[5], op[6]);
            String value_gapped = Encoder.Str2Str_Gap(value.Get());

            if (Encoder.Str2HexArray(value_gapped).length == 1) {
                value_gapped = Encoder.Byte2Word(value.Get());
            }

            Memory.Set(Encoder.Hex2Dec(op[2].Merge(op[3])), value_gapped);
        }
        else {
            System.out.println(op[1].Get() + " operator undefined at index 1.");
        }
    }

    public void GetCurrentCodeAddress(MicroCommand micro_command) {
        Hex[] op = Encoder.Str2HexArray(micro_command.Get());

        if (op[1].Equals("11")) {
            Register register = GetRegister(op[2]);
            register.Set(Encoder.Dec2Hex(CurrentCodeAddress));
        }
        else if (op[1].Equals("AA")) {
            String value_gapped = Encoder.Str2Str_Gap(Encoder.Dec2Hex(CurrentCodeAddress).Get());

            if (Encoder.Str2HexArray(value_gapped).length == 1) {
                value_gapped = Encoder.Byte2Word(Encoder.Dec2Hex(CurrentCodeAddress).Get());
            }

            Memory.Set(Encoder.Hex2Dec(op[2].Merge(op[3])), value_gapped);
        }
        else {
            System.out.println(op[1].Get() + " operator undefined at index 1.");
        }
    }

    public void Jump(MicroCommand micro_command) {
        Hex[] op = Encoder.Str2HexArray(micro_command.Get());

        if (op[1].Equals("11")) {
            CurrentCodeAddress = Encoder.Hex2Dec(GetRegister(op[2]).Get()) - 1;
        }
        else if (op[1].Equals("AA")) {
            int index = Encoder.Hex2Dec(op[2].Merge(op[3]));
            CurrentCodeAddress = Encoder.Hex2Dec(Memory.GetAt(index)[0].Merge(Memory.GetAt(index)[1])) - 1;
        }
        else {
            System.out.println(op[1].Get() + " operator undefined.");
        }
    }

    public void Increase(MicroCommand micro_command) {
        Hex[] op = Encoder.Str2HexArray(micro_command.Get());

        if (op[1].Equals("11")) {
            Register register = GetRegister(op[2]);
            register.Increase();
        }
        else if (op[1].Equals("AA")) {
            int address = Encoder.Hex2Dec(op[2].Merge(op[3]));
            String value = Encoder.HexArray2Str_NoGap(Memory.GetAt(address));
            Hex value_increased = ArithmeticHex.Add(new Hex(value), new Hex("01"));
            String value_final;

            if (Encoder.Str2HexArray(value).length == 1) {
                value_final = Encoder.Byte2Word(value_increased.Get());
            }
            else {
                value_final = value_increased.Get();
            }

            Memory.Set(address, value_final);
        }
        else {
            System.out.println(op[1].Get() + " operator undefined.");
        }
    }

    public void Decrease(MicroCommand micro_command) {
        Hex[] op = Encoder.Str2HexArray(micro_command.Get());

        if (op[1].Equals("11")) {
            Register register = GetRegister(op[2]);
            register.Decrease();
        }
        else if (op[1].Equals("AA")) {
            int address = Encoder.Hex2Dec(op[2].Merge(op[3]));
            String value = Encoder.HexArray2Str_NoGap(Memory.GetAt(address));
            Hex value_increased = ArithmeticHex.Subtract(new Hex(value), new Hex("01"));
            String value_final;

            if (Encoder.Str2HexArray(value).length == 1) {
                value_final = Encoder.Byte2Word(value_increased.Get());
            }
            else {
                value_final = value_increased.Get();
            }

            Memory.Set(address, value_final);
        }
        else {
            System.out.println(op[1].Get() + " operator undefined.");
        }
    }

    public void StartLoop() {
        Register_Lo.Set(Encoder.Dec2Hex(CurrentCodeAddress));
        Register_C.Decrease();
    }

    public void EndLoop() {
        if (!Register_C.Get().Equals("00")) {
            CurrentCodeAddress = Encoder.Hex2Dec(Register_Lo.Get());
            Register_C.Decrease();
        }
    }

    public void SetMemory() {
        Memory = new ReadOnlyMemory(50, 2);
    }

    public void AllocateVariable(MicroCommand micro_command) {
        Hex[] op = Encoder.Str2HexArray(micro_command.Get());

        Memory.Set(CurrentMemoryAddress, Encoder.Hex2Str_Word(op[1], op[2]));
        CurrentMemoryAddress++;
    }

    public void GetAddress(MicroCommand micro_command) {
        Hex[] op = Encoder.Str2HexArray(micro_command.Get());

        if (op[1].Equals("11")) {
            Register register = GetRegister(op[2]);
            register.Set(op[3].Merge(op[4]));
        }
        else if (op[1].Equals("AA")) {
            int address = Encoder.Hex2Dec(op[2].Merge(op[3]));
            Memory.Set(address, Encoder.Hex2Str_Word(op[4], op[5]));
        }
    }

    public void ExecuteROM() {
        while (!ROM.GetAt(CurrentCodeAddress)[0].Get().equals("FF")) {
            System.out.println(CurrentCodeAddress + " : " + Encoder.HexArray2Str(ROM.GetAt(CurrentCodeAddress)));
            ExecuteCommand(new MicroCommand(Encoder.HexArray2Str(ROM.GetAt(CurrentCodeAddress))));
            CurrentCodeAddress++;
        }

        System.out.println();
        GetInfo();
    }

    public void ExecuteCommand(MicroCommand micro_command) {
        Hex[] op = Encoder.Str2HexArray(micro_command.Get());

        if (op[0].Equals("01")) {
            Move(micro_command);
        }
        else if (op[0].Equals("02")) {
            AllocateVariable(micro_command);
        }
        else if (op[0].Equals("0A")) {
            ShiftRigth(micro_command);
        }
        else if (op[0].Equals("10")) {
            StartLoop();
        }
        else if (op[0].Equals("11")) {
            EndLoop();
        }
        else if (op[0].Equals("12")) {
            Jump(micro_command);
        }
        else if (op[0].Equals("A0")) {
            ShiftLeft(micro_command);
        }
        else if (op[0].Equals("AA")) {
            Multiply(micro_command);
        }
        else if (op[0].Equals("AB")) {
            Divide(micro_command);
        }
        else if (op[0].Equals("AD")) {
            Add(micro_command);
        }
        else if (op[0].Equals("AE")) {
            Subtract(micro_command);
        }
        else if (op[0].Equals("DA")) {
            GetAddress(micro_command);
        }
        else if (op[0].Equals("DD")) {
            Increase(micro_command);
        }
        else if (op[0].Equals("DE")) {
            Decrease((micro_command));
        }
        else if (op[0].Equals("EE")) {
            GetCurrentCodeAddress(micro_command);
        }
        else if (op[0].Equals("FA")) {
            SetMemory();
        }
        else if (op[0].Equals("00")) {

        }
        else {
            System.out.println(op[0].Get() + " is an unknown command.");
        }



    }

}
