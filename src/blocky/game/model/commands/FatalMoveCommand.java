package blocky.game.model.commands;

import blocky.game.model.CommandBlockyModel;

/**
 * A command to tell the model that a fatal move has been made.
 */
public class FatalMoveCommand implements ModelCommand {
  @Override
  public boolean execute(CommandBlockyModel model) throws IllegalStateException {
    model.fatalMoveMade();
    return true;
  }
}
