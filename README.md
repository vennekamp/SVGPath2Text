SVGPath2Text
============

A brute force tool to convert SVG Outlines (aka SVG Path; SVG Glyphes) back to text. Helpful if you need smaller file sizes.

A Demo-File is included. This Tool maps the Glyphs (i.e. SVG Pathes) from a template to the eleements found in a SVG File. 
To alter this work you need to change the "template letters" to your font and font size.

This is just a dirty hack ;-) It currently only works for the font  "Comic Sans MS" in file size 7px; Most of the letters in the demo file are found correctly, but placement still needs improvement.

Uses BATIK to convert Glyphs BACK to text. (http://xmlgraphics.apache.org/batik/)

The SVG File needs to be 'norrmalized' by copy it to the Source editor of Method Draw (http://editor.method.ac/) into the source editor (View --> Source) and paste it back into the SVG File.
