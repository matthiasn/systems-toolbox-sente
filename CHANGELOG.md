# Change Log
All notable changes to this project will be documented in this file. This change log follows the conventions of [keepachangelog.com](http://keepachangelog.com/).

## [v0.6.1-alpha5] - 2016-08-01
### Changed
- Sente 1.10

## [v0.6.1-alpha3] - 2016-07-11
### Changed
- Sente 1.9

## [v0.6.1-alpha2] - 2016-07-08
### Changed
- support WebJars resources


## v0.6.1-SNAPSHOT - June 17th, 2016
### BREAKING CHANGES
- :first-open message is now :sente/first-open
- Clojure 1.9 required


## v0.5.18 - June 8th, 2016
### Changed
- PR #3: Allow default sente options to be overridden


## v0.5.11 - February 24th, 2016
### Changed
- Open request counter (for asserting on all backend requests fulfilled)


## v0.5.9 - February 3rd, 2016
### Changed
- HTTP2 configurable


## v0.5.8 - February 2nd, 2016
### Changed
- don't fail when no routes-fn specified


## v0.5.7 - January 26th, 2016
### Changed
- Fixed a bug where messages buffered before WebSockets connections was open, would be sent with a `nil` `:sente-uid`.


## v0.5.6 - January 22nd, 2016
### Changed
- default no broadcast


## v0.5.5 - January 21st, 2016
### Changed
-  allow for additional routes, where the handler can communicate with the rest of the system by using the put-fn.


## v0.5.4 - January 13th, 2016
### Changed
- namespace reorganization; mock component


## v0.5.3 - January 11th, 2016
### Changed
- improved docs and logging


## v0.5.2 - January 9th, 2016
### Changed
- added SSL-support


## v0.5.1 - January 3rd, 2016
### Changed
- Sente components moved from systems-toolbox into separate repo


[v0.6.1-alpha5]: https://github.com/matthiasn/systems-toolbox-sente/compare/v0.6.1-alpha4...v0.6.1-alpha5
[v0.6.1-alpha4]: https://github.com/matthiasn/systems-toolbox-sente/compare/v0.6.1-alpha3...v0.6.1-alpha4
[v0.6.1-alpha3]: https://github.com/matthiasn/systems-toolbox-sente/compare/v0.6.1-alpha2...v0.6.1-alpha3
[v0.6.1-alpha2]: https://github.com/matthiasn/systems-toolbox-sente/compare/v0.6.1-alpha1...v0.6.1-alpha2
[v0.6.1-alpha1]: https://github.com/matthiasn/systems-toolbox-sente/compare/v0.5.18...v0.6.1-alpha1
