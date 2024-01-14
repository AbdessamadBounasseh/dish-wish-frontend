package uit.ensak.dish_wish_frontend.dto;

import java.util.List;

import uit.ensak.dish_wish_frontend.Models.Command;

public class ChefCommandHistoryDTO {

    private List<Command> CommandsInProgressForMe;
    private List<Command> CommandsFinishedForMe;
    private List<Command> CommandsInProgressByMe;
    private List<Command> CommandsFinishedByMe;

    public List<Command> getCommandsInProgressForMe() {
        return CommandsInProgressForMe;
    }

    public void setCommandsInProgressForMe(List<Command> commandsInProgressForMe) {
        CommandsInProgressForMe = commandsInProgressForMe;
    }

    public List<Command> getCommandsFinishedForMe() {
        return CommandsFinishedForMe;
    }

    public void setCommandsFinishedForMe(List<Command> commandsFinishedForMe) {
        CommandsFinishedForMe = commandsFinishedForMe;
    }

    public List<Command> getCommandsInProgressByMe() {
        return CommandsInProgressByMe;
    }

    public void setCommandsInProgressByMe(List<Command> commandsInProgressByMe) {
        CommandsInProgressByMe = commandsInProgressByMe;
    }

    public List<Command> getCommandsFinishedByMe() {
        return CommandsFinishedByMe;
    }

    public void setCommandsFinishedByMe(List<Command> commandsFinishedByMe) {
        CommandsFinishedByMe = commandsFinishedByMe;
    }
}
