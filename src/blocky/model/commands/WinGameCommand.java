package blocky.model.commands;

import blocky.model.CommandBlockyModel;

/**
 * A command to tell the model to win the game.
 */
public class WinGameCommand implements ModelCommand {
  @Override
  public boolean execute(CommandBlockyModel model) throws IllegalStateException {
    model.winGame();
    return true;
  }
}
