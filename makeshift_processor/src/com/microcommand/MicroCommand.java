package com.microcommand;

import com.code.Hex;

public class MicroCommand {

    private String command;

    public MicroCommand(String command) {
        this.command = command;
    }

    public String Get() {
        return command;
    }

}
