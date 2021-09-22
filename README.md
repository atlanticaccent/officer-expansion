# Starsector Mod Template using Gradle and IntelliJ

## Description

This is a template for a Starsector mod that uses Gradle as its build system, Kotlin as the Gradle DSL, and Kotlin as
the programming language.

One of the main goals is to move as much of the build process out of IntelliJ as possible, so that anybody can pull down
the source code and build the project with minimal hassle. IntelliJ is not required to build the mod.

Another goal is to have more project configuration as code, rather than IDE-specific files. That way, they'll get
versioned (and be shared, as mentioned).

Written for IntelliJ Community. Latest version is 2021.1 as of writing.

## Features

- A one-click command to build your mod and launch Starsector with full breakpoint debugging.
- Automatically generated `mod_info.json` and Version Checker files.
    - Set your mod's version once in `build.gradle.kts` and both files will be updated.
- A new GitHub Release will be created automatically whenever a git tag is pushed, if the mod is hosted on GitHub.
    - Delete the `.github` folder to disable this.

## Initial Setup Checklist

### Step 1

Choose whether you wish to manually update (default) the `mod_info.json` and Version Checker files or have it done automatically.

#### Option A (recommended): I will manually update my `mod_info.json` and Version Checker files

- [ ] Update `modName` in `object Variables` at the top of `build.gradle.kts`, then update the `mod_info.json`/Version Checker files by hand, as normal.
- [ ] Double-check `starsectorDirectory` in `object Variables` for correctness as well. It will need to be updated if you've installed the game to a non-default location.

#### Option B: Automatically update my `mod_info.json` and Version Checker files from a single config file

- [ ] Update all values in the `object AutoUpdateVariables` at the top of `build.gradle.kts`, then set `shouldAutomaticallyCreateMetadataFiles` to `true` in `build.gradle.kts`. Whenever you would normally manually update `mod_info.json` or Version Checker, update these values instead and the recompile and they will be updated.

### Step 2

#### Option A: If starting a brand new project

- [ ] Change the package from the template default. In IntelliJ, open up `src/main/java/com/example`,
  right-click on the first line (`package com.example`) and go to `Refactor - Rename`. From there, you may
  rename `com.example` to anything. If it pops up a refactoring preview, keep everything selected and
  click `Do Refactor`.
  - You will put any new code you write into the `src/main/java` or `/src/main/kotlin` directories.
-  Any other assets, such as `graphics` or `data`, can go directly into the top-level folder (next to, but not inside, `src`).

#### Option B: If importing existing code

- [ ] Copy the code you want to use into the `src` directory.
  - For example, if your code was in a folder structure like `data/scripts` (the .java files would start with a line like `package data.scripts;`), then the new folder structure would be `src/data/scripts`.
  - If in doubt, look at the package name at the top of a `.java` file, then look for that folder. For example, Nexerelin has a file that starts with `package exerelin;`, so we look inside Nexerelin's `jars/src.zip` and find `sources/ExerelinCore/exerelin`. We copy only the `exerelin` folder into our template's `src` folder so that the `.java` file's location relative to `src` perfectly matches the package.
  - The `main` folder in `src` can be ignored. It would be used for new projects, but is not needed for importing.
- [ ] Any other assets, such as `graphics` or `data`, can go directly into the top-level folder (next to, but not inside, `src`).

### Optional

- In `settings.gradle`, change `rootProject.name = 'template'` to be your new name instead.
  - This changes how IntelliJ itself refers to the project, but shouldn't affect anything else.
- Change `LICENSE` to something else. [Apache 2](https://tldrlegal.com/license/apache-license-2.0-(apache-2.0)) is a
  popular one.

## IntelliJ Configuration

### Ensure that your run configuration is correct:

- In IntelliJ, click `Run - Edit Configurations`.
- Select "Run Starsector"
- [ ] Set Working directory to the location of your `starsector-core` folder, if different than what's currently there.
- [ ] Check other values to make sure they fit your Starsector install. By default, they are set for a typical Windows
  install.
- Click Ok. You should now be able to choose Run Starsector from the Run menu and then click the Debug button (the icon
  of a bug) ![icon](readme_resources/debug_icon.png)
- Don't forget to enable your mod on the Starsector launcher!
- If you are running on linux, the VM Arguments should instead be

  ```-server -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005 -Djava.library.path=./native/linux -Xms1536m -Xmx1536m -Xss2048k -classpath janino.jar:commons-compiler.jar:commons-compiler-jdk.jar:starfarer.api.jar:starfarer_obf.jar:jogg-0.0.7.jar:jorbis-0.0.15.jar:json.jar:lwjgl.jar:jinput.jar:log4j-1.2.9.jar:lwjgl_util.jar:fs.sound_obf.jar:fs.common_obf.jar:xstream-1.4.10.jar -Dcom.fs.starfarer.settings.paths.saves=./saves -Dcom.fs.starfarer.settings.paths.screenshots=./screenshots -Dcom.fs.starfarer.settings.paths.mods=./mods -Dcom.fs.starfarer.settings.paths.logs=. -Dcom.fs.starfarer.settings.linux=true com.fs.starfarer.StarfarerLauncher```

*Example for this template*

![Final Run Configuration](screenshots/runConfig.png "Final Run Configuration")

## Adding new libraries as dependencies

By default, only LazyLib is added. To add other mod dependencies, open `build.gradle.kts` and navigate down to the `dependencies` section. 

The easiest thing to do is to copy the existing LazyLib dependency, which starts with `compileOnly`, and modify it to point to the folder containing the .jar file(s) of the mod you're adding.

After making any change to the `build.gradle.kts` file, click the "Load Gradle changes" button that should have appeared in the top-right corner.

![gradle sync](readme_resources/gradle_build.png)

## Other

Author: Wispborne (Wisp#0302 on the Unofficial Starsector Discord)

License: [Unlicense](https://github.com/davidwhitman/starsector-mod-template/blob/master/LICENSE)
