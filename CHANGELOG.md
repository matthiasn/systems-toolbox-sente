# Change Log
All notable changes to this project will be documented in this file. This change log follows the conventions of [keepachangelog.com](http://keepachangelog.com/).

## [0.6.19] - 2017-10-26
### Changed
- always relay firehose messages

## [0.6.18] - 2017-10-26
### Changed
- dependencies

## [0.6.17] - 2017-09-20
### Changed
- no relaying of sente-internal messages

## [0.6.16] - 2017-08-24
### Changed
- latest dependencies

## [0.6.15] - 2017-05-30
### Changed
- latest Clojure and ClojureScript versions after the spec split

## [0.6.14] - 2017-04-17
### Changed
- using :client-id as user-id

## [0.6.13] - 2017-04-13
### Changed
- latest deps in tests

## [0.6.12] - 2017-04-11
### Changed
- random-user-id-fn removed - custom user-id-fn can be set in :sente-opts

## [0.6.11] - 2017-04-01
### Changed
- Allow ring-defaults' defaults to be supplied from the outside

## [0.6.9] - 2017-03-30
### Changed
- flexible user routes

## [0.6.7] - 2017-03-17
### Changed
- latest deps

## [0.6.6] - 2017-03-10
### Changed
- latest deps

## [0.6.5] - 2017-01-19
### Changed
- latest immutant 

## [0.6.4] - 2017-01-12
### Changed
- latest deps, including the security fix for ring

## [0.6.3] - 2017-01-09
### Changed
- fixed problem with new handler result validation

## [0.6.2] - 2017-01-09
### Changed
- improved logging
- latest deps

## [0.6.1] - 2016-11-23
### Changed
- moving away from alpha status
- Clojure 1.9 just works, and because of clojure.spec, you should adopt it, too

## [v0.6.1-alpha14] - 2016-11-23
### Changed
- latest dependencies
- added test for broadcast messages
- mock namespace moved to test dir

## [v0.6.1-alpha13] - 2016-10-23
### Changed
- latest ClojureScript
- latest Selenium in tests
- tests working in Firefox again

## [v0.6.1-alpha12] - 2016-10-16
### Changed
- code coverage using lein-cloverage

## [v0.6.1-alpha11] - 2016-10-15
### Changed
- integration tests added

## [v0.6.1-alpha10] - 2016-10-13
### Changed
- updated dependencies

## [v0.6.1-alpha9] - 2016-10-12
### Changed
- Prioritize user routes over default ones (PR from BartAdv)

## [v0.6.1-alpha8] - 2016-09-25
### Changed
- dependency cleanup

## [v0.6.1-alpha7] - 2016-08-24
### Changed
- updated dependencies

## [v0.6.1-alpha6] - 2016-08-17
### Changed
- change priorities when setting port: undertow-cfg > ENV > cfg > default

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
