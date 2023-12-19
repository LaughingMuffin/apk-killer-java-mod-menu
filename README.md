# Android Java - Mod Menu - Template

## Build & Run
- [Android Studio](https://developer.android.com/studio)
- [Intellij Idea Free / Ultimate](https://www.jetbrains.com/idea/)
- AndroidIDE | AIDE - Might fail, if it does big RIP! Will check maybe...

## Features
- [x] Mod Menu based on LGL v3.2
  - The best Java menu out there
- [x] Apk Killer
  - It can bypass signature & integrity checks, might fail on some advanced or custom ones
- [x] Multi tabs in menu header
  - Some extra stuff I was keeping to myself over the past years
- [x] Java & C++ Obfuscator
- [x] Hooking Frameworks
  - Dobby and xDL for now 
- [x] Load Lib from Assets (Simple Java Example)
  - A simple example has been provided, it's not the best out there but I hope it gives a general idea.
- [ ] Load Lib from Assets (Simple C++ Example)
- [ ] Memory Patch Tool

### Load lib from assets
This is nothing more than extracting the file from assets to a folder like "data dir" then loading it 
in memory using System.load() function. Used library for this example can be found in this 
project here, just build it and that's it [Dummy Lib coming from here](https://github.com/LaughingMuffin/Dummy-Android-Project).
You can see it being loaded by looking at logs with this TAG: DUMMY-MUFFIN

## How to add to any app
- Build this project
- Decompile output APK
- Extract smali & lib
- Decompile target app
- Put inside decompiled target app your smali and lib
- Recompile, Sign & Test

### HELP
- [How to decompile / recompile APK](https://youtu.be/xWU5Tk3MizY)
- [How to sign an APK](https://youtu.be/GwkQelv3cGk)
- [How to load your own lib from smali](https://youtu.be/JKwPPwnVehw)
- Full implementation video - Coming soon!

## Result

![menu_open_full.png](screenshots%2Fmenu_open_full.png)

## Credits

Mod Menu source - [LGL Android Mod Menu](https://github.com/LGLTeam/Android-Mod-Menu)<br>
Apk Killer source - [APKKiller](https://github.com/aimardcr/APKKiller)<br>
Obfuscator (C++) - [Obfuscate by adamyaxley](https://github.com/adamyaxley)<br>
Obfuscator (Java) - [StringFog](https://github.com/MegatronKing/StringFog)<br>
xDL - [xDL by hexhacking](https://github.com/hexhacking/xDL)<br>
Dobby - [Dobby by jmpews](https://github.com/jmpews/Dobby)<br>
___
Color Picker - [Online Color Picker](https://rgbcolorpicker.com/0-1)<br>
___
###### Android Java - Mod Menu - Template is licensed under the MIT License, see [LICENSE](license.txt) for more information.
___