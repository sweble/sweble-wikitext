# Wikipedia Markup Template

Wikipeida contains two kinds of markup templates:

* Magic Words
* Extension Tags


## Magic Words 

[Magic Words] (https://www.mediawiki.org/wiki/Help:Magic_words) are build-in placeholders for site 
statistics and environment values.

The [definition](https://doc.wikimedia.org/mediawiki-core/master/php/MagicWord_8php_source.html) of 
the tags is done directly in the WikiMedia code.


## Template Extension

Additional template tags are handled by external parsers, which are included as extensions, and 
therefore are not in the default WikiMedia repository. A description on how to add those extensions 
could be found at the [Parser Functions Manual](https://www.mediawiki.org/wiki/Manual:Parser_functions).

For example the [Coord](https://www.mediawiki.org/wiki/Template:Coord/doc) template which 
represents, translates and converts coordinates, is part of the 
[GeoData](https://phabricator.wikimedia.org/diffusion/EGDA/browse/master/) extension. This template 
tag is currently used in over 1 Million Wikipedia articles.

Another common used template tag is [Cite web](https://meta.wikimedia.org/wiki/Template:Cite_web/doc) 
or the conversion for standard measurement units [Convert](https://en.wikipedia.org/wiki/Template:Convert/doc).

