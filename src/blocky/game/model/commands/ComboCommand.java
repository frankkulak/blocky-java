package blocky.game.model.commands;

import blocky.game.model.CommandBlockyModel;

/**
 * A command that is a combination of two other commands. May use recursively for infinite chaining.
 * The first command given in the constructor is executed first.
 */
public class ComboCommand implements ModelCommand {
  private final ModelCommand command1, command2;

  /**
   * Creates new ComboCommand out of the two given commands.
   *
   * @param c1 priority command in this combo
   * @param c2 secondary command in this combo
   */
  public ComboCommand(ModelCommand c1, ModelCommand c2) {
    this.command1 = c1;
    this.command2 = c2;
  }

  @Override
  public boolean execute(CommandBlockyModel model) throws IllegalStateException {
    boolean c1 = this.command1.execute(model);
    boolean c2 = this.command2.execute(model);
    return c1 || c2;
  }
}
