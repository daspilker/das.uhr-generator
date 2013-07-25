DAS.UHR.Generator
=================

This repository contains some code which generates design files for laser cutting a word clock.

Have a look at the [DAS.UHR photo set](http://www.flickr.com/photos/daspilker/sets/72157634777656109/) for an example.


How-to
------

To generate the design files you need to download and install the DIN Schablonierschrift font mentioned below. Make sure you have a recent version of Java 7 installed and then run the `gradlew` script.

The generator produces 6 SVG files. You can use [Inkscape](http://inkscape.org/) to import the generated designs into the templates provided by [Ponoko](https://www.ponoko.com/) or [Formulor](http://www.formulor.de/).

The parameters which control the generator can be tweaked by editing the `com.daspilker.uhr.svggenerator.Main` class. 


Font
----

For generating the front panel, a stencil font must be used. You can use the free font DIN Schablonierschrift by Marian Steinbach. The font is not included in this repository, but you can download it from the author's web site.

http://www.sendung.de/dinschablonierschrift/


License
-------

Copyright 2012 Daniel A. Spilker

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
