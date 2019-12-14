package blocky.game.model.commands;

import blocky.game.model.CommandBlockyModel;

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
