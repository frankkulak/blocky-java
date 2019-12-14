package blocky.game.model.commands;

import blocky.game.model.CommandBlockyModel;

/**
 * A command to do nothing. This exists to reduce null pointers and for readability.
 */
public class DoNothingCommand implements ModelCommand {
  @Override
  public boolean execute(CommandBlockyModel model) throws IllegalStateException {
    return false;
  }
}
