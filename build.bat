mkdir bin

javac -source 1.6 -target 1.6 -cp .;bin\;libs\codesize-1.1.jar;libs\picocontainer-2.14.2.jar;libs\robocode.battle-1.8.3.0.jar;libs\robocode.core-1.8.3.0.jar;libs\robocode.host-1.8.3.0.jar;libs\robocode.jar;libs\robocode.repository-1.8.3.0.jar;libs\robocode.sound-1.8.3.0.jar;libs\robocode.ui-1.8.3.0.jar;libs\robocode.ui.editor-1.8.3.0.jar;libs\roborumble.jar -d bin src\*

