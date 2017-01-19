# systems-toolbox-sente

This library gives you WebSockets communication between a Clojure backend and ClojureScript web clients. One of the applications using it is **[BirdWatch](https://github.com/matthiasn/Birdwatch)**, which is a good example for an application where information continuously flows from the server to the connected client, rather than only answering a few requests on page load.

In addition, the server side component also allows specifying routes and their handlers so that REST requests can also be answered.

These components have previously been part of the **[system-toolbox](https://github.com/matthiasn/systems-toolbox)** library and were moved into a separate repository to reduce dependencies.

[![Dependencies Status](https://jarkeeper.com/matthiasn/systems-toolbox-sente/status.svg)](https://jarkeeper.com/matthiasn/systems-toolbox-sente)

## Testing

As a default, the tests will run in **Chrome**. This requires you to install **[ChromeDriver](https://sites.google.com/a/chromium.org/chromedriver/)** first:

    $ bin/get-chromedriver.sh

With ChromeDriver installed, you can fire up the tests:

    $ lein integrations-tests


Also, you can run the tests using **[PhantomJS](http://phantomjs.org/)**:

    $ BROWSER=phantomjs lein integration-tests


Tests are run automatically on **CircleCI** using Chrome: [![CircleCI Build Status](https://circleci.com/gh/matthiasn/systems-toolbox-sente.svg?&style=shield)](https://circleci.com/gh/matthiasn/systems-toolbox-sente)

On **TravisCI**, the tests are run on **[PhantomJS](http://phantomjs.org/)**: [![TravisCI Build Status](https://travis-ci.org/matthiasn/systems-toolbox-sente.svg?branch=master)](https://travis-ci.org/matthiasn/systems-toolbox-sente)


Test coverage can also be checked:

    $ lein test-coverage


## License

Copyright © 2015, 2016 Matthias Nehlsen

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
