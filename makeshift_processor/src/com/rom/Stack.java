package com.rom;

import java.util.Dictionary;

public class Stack {

    Dictionary<String, Integer> values;

    public Stack() {}

    public int GetAt(String name) {
        return values.get(name);
    }


}
