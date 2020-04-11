# Blocky (Java)
			
**Created:** April 12, 2019 | **Last updated:** May 21, 2019

_Blocky_ is an original puzzle game in which the player must navigate a block through obstacles in order to reach the exit. Levels must be completed with the least number of moves possible.

Please keep in mind that this code has not been touched (other than some comments, styling, and creation of a repo) since May of 2019. My programming style has changed dramatically since then; please also see my re-creation of [_Block Dude_](https://github.com/frankkulak/blockdude-java) for a more recent example of my coding practices.

## Playing the game

#### How to run the game

There are two ways to run _Blocky_ from your IDE:
- A text-based view that runs in the console
- A more visual view created with Java Swing

**Quick setup:** To run the visual view using high-quality graphics with the introductory levels, use `run -view dark 350 high -file introLevels.txt` for the run configurations.

<details>
  <summary>Advanced setup instructions</summary>
  
  **Run configurations**
  
  The initial argument of the run configurations determines what the program will do when it runs.
  - `run`: Runs the game.
  - `solve`: Finds solutions to all levels within a given file.
  - `gen`: Generates random levels (outputs to console in level data file format).
  
  **Arguments to follow `run`**
  - `-view <type> <size> <quality>`: Required argument. Specifies the view to be used in rendering the game.
    - `<type>`: Required argument. Specifies the view to use for running the game. Must be one of `text` (runs in console), `dark` (runs user-friendly view with dark theme), or `light` (runs user-friendly view with light theme).
    - `<size>`: Required for use with `dark` and `light` views only. Specifies the size of the view in pixels. Must be an integer greater than or equal to 50 (e.g. `350`).
    - `<quality>`: Required for use with `dark` and `light` views only. Specifies the quality (i.e. smoothness) of animations. Must be one of `low`, `med`, `high`, or `best`.
  - `-file <filename>`: Required argument (if and only if `-random` is NOT used). Specifies the file from which to read level data.
    - `<filename>`: Required argument. Specifies the name of the file from which to read level data. Must be the name of a file within the levelData directory, including the file extension (e.g. `introLevels.txt`).
  - `-random <size> <moves>`: Required argument (if and only if `-file` is NOT used). Specifies that levels should be randomly generated with the given properties.
    - `<size>`: Required argument. Specifies the width and height of the game board (i.e. how many blocks can fit horizontally and vertically). Must be an integer greater than or equal to 1; odd numbers 5-9 recommended (e.g. `5`).
    - `<moves>`: Required argument. Specifies the lowest number of moves that levels should require (i.e. "only generate levels that require this many moves or more"). Must be an integer greater than or equal to 1; lower values recommended (e.g. `3`).
    - **WARNING**: Using a value that is very low (1-3), or very high (10+) for `<size>` or a value that is very high (10+) for `<moves>` can hinder performance and may put the game in a state where it is impossible to generate levels, causing the game to lag, freeze, or crash. For this reason, please use values within the recommended ranges.
  
  **Arguments to follow `solve`**
  - `-file <filename>`: Required argument. Specifies the file from which to read level data.
  - `<filename>`: Required argument. Specifies the name of the file from which to read level data. Must be the name of a file within the levelData directory, including the file extension (e.g. `introLevels.txt`).
  
  **Arguments to follow `gen`**
  - `<size>`: Required argument. Specifies the width and height of the game board (i.e. how many blocks can fit horizontally and vertically). Must be an integer greater than or equal to 1; odd numbers 5-9 recommended (e.g. `5`).
  - `<moves>`: Required argument. Specifies the lowest number of moves that levels should require (i.e. "only generate levels that require this many moves or more"). Must be an integer greater than or equal to 1; lower values recommended (e.g. `3`).
  - `<quantity>`: Required argument. Specifies the number of levels to generate. Must be an integer greater than or equal to 1.
  - **WARNING**: Using a value that is very low (1-3), or very high (10+) for `<size>` or a value that is very high (10+) for `<moves>` can put the generator in a state where it is impossible to generate levels, causing the program to freeze. For this reason, please use values within the recommended ranges.
</details>

#### How to play the game

<details>
  <summary>Text-based view controls</summary>
  
  Type the following and press enter
  - `w`: Move up.
  - `a`: Move left.
  - `s`: Move down.
  - `d`: Move right.
  - `/re`: Restart level.
  - `/quit`: Quit the game.
</details>

<details>
  <summary>Visual view controls</summary>
  
  Press the corresponding keys
  - `w` or `<up arrow>`: Move up.
  - `a` or `<left arrow>`: Move left.
  - `s` or `<down arrow>`: Move down.
  - `d` or `<right arrow>`: Move right.
  - `r`: Restart level.
  - `esc`: Quit the game.
</details>

More advanced controls & cheat codes can be found in TextBlockyView.java or VisualBlockyView.java in the `handleCommand()` methods.

## Bugs and known issues

**Game sometimes lags, freezes when generating levels** (Noticed: Dec 14 2019; Priority: Minor)
- _Description_: The game may lag or freeze when generating levels, due to the inefficient way that levels are currently being generated. This is generally not an issue when normal values are given for the size and moves required for levels.
- _Temporary solution_: Do not use extreme size or moves values for generating levels.
- _Ideas for fix_: Rewrite the way that levels are generated to be more efficient; prevent extreme values from being accepted by the program.

**Game crashes due to infinite recursion** (Noticed: Apr 18 2019; Priority: Minor)
- _Description_: If a block is between two blue blocks and hits one of them, it will bounce into the
   other blue block, then back, and so on infinitely until `StackOverflowException` is thrown.
- _Temporary solution_: Do not create any levels with blue blocks back-to-back, and prevent level generator from creating any such levels.
- _Ideas for fix_: Have model keep track of whether or not BlueBlock has been hit already on this move, but this would be brittle for the future (idea for blue corner pieces, etc.).

**Runtime issue, game freezes** (Noticed: May 21 2019; Priority: None)
- _Description_: If either the 'n' or 'e' key is held, user input from the keyboard will no longer be
   accepted until the game is restarted. After looking into this issue online, it seems to be a known issue with macOS Sierra.
- _Temporary solution_: Avoid holding 'n' or 'e'; relaunch game after freeze occurs; don't use macOS Sierra.
- _Ideas for fix_: Allow mouse input from user; research ways around bug.

## Future plans

Refactor codebase:
- Convert the `gamepieces` package to an enum
- Convert the `commands` package to an enum
- De-couple the model and visual view (remove use of 'sprites')
- Remake the level-generation algorithm as something more efficient

Add new types of blocks:
- Orange (hit once, becomes red)
- Purple (moves like yellow, destroys what it hits)
- Green (stops blocks that go past it (even adjacent))
- Blue corner piece (like blue, except changes player direction some other way)
