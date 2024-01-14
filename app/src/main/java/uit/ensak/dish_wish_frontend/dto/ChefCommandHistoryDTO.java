package uit.ensak.dish_wish_frontend.dto;

import java.util.List;

import uit.ensak.dish_wish_frontend.Models.Command;

public class ChefCommandHistoryDTO {

    private List<Command> commandsInProgressForMe;
    private List<Command> commandsFinishedForMe;
    private List<Command> commandsInProgressByMe;
    private List<Command> commandsFinishedByMe;

    public List<Command> getCommandsInProgressForMe() {
        return commandsInProgressForMe;
    }

    public void setCommandsInProgressForMe(List<Command> commandsInProgressForMe) {
        this.commandsInProgressForMe = commandsInProgressForMe;
    }

    public List<Command> getCommandsFinishedForMe() {
        return commandsFinishedForMe;
    }

    public void setCommandsFinishedForMe(List<Command> commandsFinishedForMe) {
        this.commandsFinishedForMe = commandsFinishedForMe;
    }

    public List<Command> getCommandsInProgressByMe() {
        return commandsInProgressByMe;
    }

    public void setCommandsInProgressByMe(List<Command> commandsInProgressByMe) {
        this.commandsInProgressByMe = commandsInProgressByMe;
    }

    public List<Command> getCommandsFinishedByMe() {
        return commandsFinishedByMe;
    }

    public void setCommandsFinishedByMe(List<Command> commandsFinishedByMe) {
        this.commandsFinishedByMe = commandsFinishedByMe;
    }
}