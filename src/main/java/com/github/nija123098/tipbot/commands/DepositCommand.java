package com.github.nija123098.tipbot.commands;

import com.github.nija123098.tipbot.AbstractCommand;
import com.github.nija123098.tipbot.Command;
import com.github.nija123098.tipbot.Database;
import com.github.nija123098.tipbot.utility.Config;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static com.github.nija123098.tipbot.Database.RECEIVING_ADDRESSES_TABLE;

public class DepositCommand extends AbstractCommand {
    @Override
    public String getHelp() {
        return "Deposits Dash into the bot's wallet to enable tipping.";
    }

    @Override
    public Command getCommand() {
        return (invoker, arguments, channel) -> {
            String address = Database.getValue(RECEIVING_ADDRESSES_TABLE, invoker, null);
            if (address == null){
                //TODO: this is very bad for performance, no error checking, tons of things can go wrong here with starting a process and hoping it al works
                Process process = new ProcessBuilder("dash-cli", "-rpcuser="+Config.RPC_USER, "-rpcpassword="+ Config.RPC_PASS, "getnewaddress").start();
                address = new BufferedReader(new InputStreamReader(process.getInputStream())).readLine();
                Database.setValue(RECEIVING_ADDRESSES_TABLE, invoker, address);
            }
            return "Funds deposited to the following address will be attributed to you.\n" + address;
        };
    }
}
