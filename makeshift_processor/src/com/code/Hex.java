package com.code;

public class Hex {

    private String number;

    public Hex() {
        number = "00";
    }
    public Hex(String number) {
        this.number = number;
    }

    public String Get() {
        return number;
    }

    public void Set(String number) {
        this.number = number;
    }
    public void Set(Hex number) {
        this.number = number.Get();
    }

    public boolean Equals(String value) {
        return number.equals(value);
    }

    public Hex Merge(Hex value) {
        return new Hex(number + value.Get());
    }


}
