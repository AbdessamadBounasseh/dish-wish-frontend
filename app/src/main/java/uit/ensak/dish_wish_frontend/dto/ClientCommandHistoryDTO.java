package uit.ensak.dish_wish_frontend.dto;

import java.util.List;

import uit.ensak.dish_wish_frontend.Models.Command;

public class ClientCommandHistoryDTO {

    private List<Command> CommandsInProgress;
    private List<Command> CommandsFinished;

    public List<Command> getCommandsInProgress() {
        return CommandsInProgress;
    }

    public void setCommandsInProgress(List<Command> commandsInProgress) {
        CommandsInProgress = commandsInProgress;
    }

    public List<Command> getCommandsFinished() {
        return CommandsFinished;
    }

    public void setCommandsFinished(List<Command> commandsFinished) {
        CommandsFinished = commandsFinished;
    }
}
