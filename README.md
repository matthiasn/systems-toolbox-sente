# systems-toolbox-sente

This library gives you WebSockets communication between a Clojure backend and ClojureScript web clients. One of the applications using it is **[BirdWatch](https://github.com/matthiasn/Birdwatch)**, which is a good example for an application where information continuously flows from the server to the connected client, rather than only answering a few requests on page load.

In addition, the server side component also allows specifying routes and their handlers so that REST requests can also be answered.

These components have previously been part of the **[system-toolbox](https://github.com/matthiasn/systems-toolbox)** library and were moved into a separate repository to reduce dependencies.


## License

Copyright © 2015, 2016 Matthias Nehlsen

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
