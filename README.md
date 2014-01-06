Thea Explorer
=============

This Play! application has been created to easily display files contained on a server (web interface). Only tested on Unix systems for the moment.
Play 2.2.1 is used.


Configuration
=============

The configuration is managed in a file located in `conf/customParameters.properties`. The parameters are :

- `root-folder=/your/root/folder`

The folder which will be displayed in the dashboard. It can be changed using the application.

- `user=root` and `password=root`

Manage the authentification parameters.


Known issues
------------

- FileNotFoundException - Too many open files

If you try to display a folder too big or too close from the root, you may have a java.io.File exception : "FileNotFoundException - Too many open files". This is do to your system configuration.
Run `ulimit -a` and check the `open files` line. If you do want to change this limit, run `ulimit -n 90000 && ulimit -u 90000` (or whatever the value you want to set.)

- File name special characters

If some file names are not well displayed (and file size == 0) try to run `export LC_CTYPE="UTF-8"` before launching `play run`.
