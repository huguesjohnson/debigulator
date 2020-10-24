# debigulator
## History 

One day in 2003 I was backing up my hard drive and thought "gee, what I really need is an archive program that compresses a list of files into individual archives." So I did a search for "batch zip programs" and didn't find anything worthwhile. There were some commercial utilities that sort of did what I wanted (all are out of business now) and couple "free" (ad-laden) ones. So I decided to write my own and roughly 30 days later a program called Debigulator was born.

I was fairly happy with the first version of Debigulator, it worked for me but maybe wasn't great for others. Out of nowhere in 2007 I got the itch to revive Debigulator to make it more user-friendly. This resulted in Debigulator 1.1, it's remarkably similar to the original but a tad simpler to navigate.

One random day in 2014 I decided I hated the user interface and rewrote it in JavaFX. Although it looks much nicer it only runs on Java 8+ so both the original version and this fancy new one are available here.

## Features

* Archives files into individual archives. This is a very handy way to compress your collection of completely legal game ROMs onto your phone's SD card (so I hear).

* Writes archives in either .zip or .jar format. The JavaFX version also supports GZip and has experimental support for 7zip.

* Save/load sessions, useful if you want to run the same backup job multiple times.

* Verify that the contents of archives match original files after creating them.

* Totally free and open-source with no ads. 

## Development Status

I occasionally update this when there are major releases of Java. The last update was done for the JavaFX version on Java 11. The Swing version will likely never be updated again.

## Other Notes

This project uses Tango Icons: https://commons.wikimedia.org/wiki/Tango_icons

