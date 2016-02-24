## v0.5.11 - February 24th, 2016

```clojure
[matthiasn/systems-toolbox-sente "0.5.11"]
```

* Open request counter (for asserting on all backend requests fulfilled)


## v0.5.9 - February 3rd, 2016

```clojure
[matthiasn/systems-toolbox-sente "0.5.9"]
```

* HTTP2 configurable


## v0.5.8 - February 2nd, 2016

```clojure
[matthiasn/systems-toolbox-sente "0.5.8"]
```

* don't fail when no routes-fn specified


## v0.5.7 - January 26th, 2016

```clojure
[matthiasn/systems-toolbox-sente "0.5.7"]
```

* Fixed a bug where messages buffered before WebSockets connections was open, would be sent with a `nil` `:sente-uid`.


## v0.5.6 - January 22nd, 2016

```clojure
[matthiasn/systems-toolbox-sente "0.5.6"]
```

* default no broadcast


## v0.5.5 - January 21st, 2016

```clojure
[matthiasn/systems-toolbox-sente "0.5.5"]
```

*  allow for additional routes, where the handler can communicate with the rest of the system by using the put-fn.


## v0.5.4 - January 13th, 2016

```clojure
[matthiasn/systems-toolbox-sente "0.5.4"]
```

* namespace reorganization; mock component


## v0.5.3 - January 11th, 2016

```clojure
[matthiasn/systems-toolbox-sente "0.5.3"]
```

* improved docs and logging


## v0.5.2 - January 9th, 2016

```clojure
[matthiasn/systems-toolbox-sente "0.5.2"]
```

* added SSL-support


## v0.5.1 - January 3rd, 2016

```clojure
[matthiasn/systems-toolbox-sente "0.5.1"]
```

* Sente components moved from systems-toolbox into separate repo
