# Change Log
[A guide to writing change logs][keepachangelog]

## Unreleased

## 3.1.0 - 2016-06-13
### Changed
- Document automatically generated during deserialization in 
  sweble-wom3-json-tools if not doc is explicitly set does not contain article 
  element by default any more.
- Generalized sweble-wom3-json-tools code to work with w3c docs as well (BREAKS INTERFACE)
- Generalized sweble-engine-serialization code to work with w3c docs as well (BREAKS INTERFACE)
- Generalized Wom3Toolbox code to work with w3c docs as well (BREAKS INTERFACE)

### Added
- WomToolbox.{isWomElement, isRtd, isText, isRtdOrText} methods
- WomSerializer.setDocumentImplClassName method

## 3.0.2 - 2016-06-07
### Changed
- Bumped version of tooling parent pom and osr-common dependencies to 3.0.3

## 3.0.1 - 2016-05-03
### Fixed
- Illegal characters had no rtd assigned

### Added
- Added parser configuration options to turn off foster parenting in 
  post-processing

### Changed
- Bumped version of tooling parent pom and osr-common dependencies to 3.0.2

[keepachangelog]: http://keepachangelog.com/
