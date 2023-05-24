# [SWEN30006 2023S1] Project Assignment 2
![PacMan](pacman/sprites/ghost_0.gif)
PacMan in the TorusVerse
> Project by Team 7 @Friday 1pm

# Team Members
![PacMan](pacman/sprites/pacpix_0.gif) 1264462 [Angel He](https://github.com/7angel4) (<angelh1@student.unimelb.edu.au>)

![PacMan](pacman/sprites/pacpix_1.gif) 1180051 [Ziming Wang](https://github.com/Ziming-W) (<zimiwang@student.unimelb.edu.au>)

![PacMan](pacman/sprites/pacpix_2.gif) 1238958 [Weida Fan](https://github.com/Vaida12345) (<weidaf@student.unimelb.edu.au>)


Note:
* All paths, unless otherwise specified, are relative to the `pacman` folder.
* The `maps` folder contains our own testing maps for the app.
* The `sprites` folder contains both the sprites used by the game and the editor's data.
* The game and level checking will report to `pacman/errorLog.txt`.
* The `GameCallBack` will report to `pacman/Log.txt`
* Only the `src` folder contains code for this project.
* `src.Driver.main()` is the entry point for this application.
* The `src.TestPrograms` package is for our own testing. It is not part of the functionalities required by the spec.
* As per the announcement, the default properties file for our game is `pacman/properties/test.properties`.
* Clarification for some unclear parts of the spec:
  * For a valid game folder passed as argument, we allow the editor (with no current map) and game window to be opened at the same time. The user can just close the game window once it's completed.
  * If a specific map is passed as the command-line argument, the editor will start on edit mode on that map. Then, you can press the "Start Game" button to test the map.
  * Please provide the relative path as the command-line argument, e.g.:
    * game folder: `pacman/maps/Sample_Game`
    * map file: `pacman/maps/Sample_Game/1SpecMap.xml`
  * For error-logging:
    * We only keep the filename portion for a map file, as given in the example in the spec.
    * But we keep the full directory for a game folder (no example given in the spec).