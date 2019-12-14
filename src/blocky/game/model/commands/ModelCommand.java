package blocky.game.model.commands;

import blocky.game.model.CommandBlockyModel;

/**
 * Represents a command that may arise when game pieces interact with one another.
 */
public interface ModelCommand {
  /**
   * Executes this command, returns whether it did anything.
   *
   * @param model model to perform command on
   * @return whether or not this command changed anything
   * @throws IllegalStateException if command cannot currently be executed
   */
  boolean execute(CommandBlockyModel model) throws IllegalStateException;
}
