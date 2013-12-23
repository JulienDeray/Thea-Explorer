Thea Explorer
=============

This Play! application has been created to easily display files contained on a server (web interface). Only tested on Unix systems for the moment.


Known issues
------------

If you try to display a folder too big or too close from the root, you may have a java.io.File exception : "FileNotFoundException - Too many open files". This is do to your system configuration.
Run `ulimit -a` and check the `open files` line. If you do want to change this limit, run `ulimit -n 90000 && ulimit -u 90000` (or whatever the value you want to set.)
