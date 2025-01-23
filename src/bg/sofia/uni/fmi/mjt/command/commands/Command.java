package bg.sofia.uni.fmi.mjt.command.commands;

import bg.sofia.uni.fmi.mjt.database.DataBase;

public interface Command {

    String execute(DataBase dbConnection);
}
