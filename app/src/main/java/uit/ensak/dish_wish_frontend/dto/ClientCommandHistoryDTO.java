package uit.ensak.dish_wish_frontend.dto;

import java.util.List;

import uit.ensak.dish_wish_frontend.Models.Command;

public class ClientCommandHistoryDTO {

    private List<Command> commandsInProgress;
    private List<Command> commandsFinished;

    public List<Command> getCommandsInProgress() {
        return commandsInProgress;
    }

    public void setCommandsInProgress(List<Command> commandsInProgress) {
        this.commandsInProgress = commandsInProgress;
    }

    public List<Command> getCommandsFinished() {
        return commandsFinished;
    }

    public void setCommandsFinished(List<Command> commandsFinished) {
        this.commandsFinished = commandsFinished;
    }
}