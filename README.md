# sweble-wikitext

The Sweble Wikitext Components module provides a parser for MediaWiki's wikitext and an engine trying to emulate the behavior of a MediaWiki.

## How to use

This library is published on Maven Central. You can use it by adding the following Maven coordinates to your project:

```
<dependency>
  <groupId>org.sweble.wikitext</groupId>
  <artifactId>swc-parser-lazy</artifactId>
  <version>3.1.9</version>
</dependency>
```
This module exposes the wikitext parser itself, [other Maven modules are available as well](https://search.maven.org/search?q=org.sweble.wikitext).

## How to set up the library for development

You first need to clone the [sweble/osr-common](https://github.com/sweble/osr-common) repository, and then clone this repository inside it, as the `tooling/sweble-wikitext` local directory.
You can then work on this library as a Maven project.
