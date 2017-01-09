#!/usr/bin/env bash

cd "$( dirname "${BASH_SOURCE[0]}" )"

case "$OSTYPE" in

  darwin*)
    wget https://github.com/mozilla/geckodriver/releases/download/v0.13.0/geckodriver-v0.13.0-macos.tar.gz
    tar -xzf geckodriver-v0.13.0-macos.tar.gz
    rm geckodriver-v0.13.0-macos.tar.gz
    ;;

  linux*)
    wget https://github.com/mozilla/geckodriver/releases/download/v0.13.0/geckodriver-v0.13.0-linux64.tar.gz
    tar -xzf geckodriver-v0.13.0-linux64.tar.gz
    rm geckodriver-v0.13.0-linux64.tar.gz
    ;;

  *)        echo "unknown: $OSTYPE" ;;
esac

cd -
