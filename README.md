SVGPath2Text
============

A brute force tool to convert SVG Outlines (aka SVG Path; SVG Glyphes) back to text. Helpful if you need smaller file sizes.

A Demo-File is included. This Tool maps the Glyphs (i.e. SVG Pathes) from a template to the elements found in a SVG File. 
To alter this work you need to change the "template letters" to your font and font size.

This is just a dirty hack ;-) It currently only works for the font  "Comic Sans MS" in file size 7px; Most of the letters in the demo file are found correctly, but placement still needs improvement.

Uses BATIK to convert Glyphs BACK to text. (http://xmlgraphics.apache.org/batik/) You need to add the approbiate LIBS to the project. (http://www.cs.hs-rm.de/~knauf/SWTProjekt2008/batik/index.html)

The SVG File needs to be 'norrmalized' by copy it to the Source editor of Method Draw (http://editor.method.ac/) into the source editor (View --> Source) and paste it back into the SVG File.

To use this HACK copy it to NETBEANS (8 w/ Java 8) and run "ExtraktLettersFromPath.java" then edit the word puzzle in INKSPACE (http://www.inkscape.org/de/) and run "RemoveAllRedPathes.java" to remove all obsolete pathes.
