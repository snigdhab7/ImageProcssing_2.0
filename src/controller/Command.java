package controller;

/**
 * The Command interface represents an executable command in the application.
 * Classes implementing this interface must provide a concrete implementation
 * for the run method, which executes the command with the given parameters.
 */
public interface Command {
  /**
   * Executes the command with the provided parameters.
   *
   * @param commandParams An array of strings representing the parameters
   *                      required for the execution of the command.
   */
  void run(String[] commandParams);
}
