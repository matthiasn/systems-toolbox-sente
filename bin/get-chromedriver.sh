#!/usr/bin/env bash

cd "$( dirname "${BASH_SOURCE[0]}" )"

case "$OSTYPE" in

  darwin*)
    wget http://chromedriver.storage.googleapis.com/2.24/chromedriver_mac64.zip
    unzip -o chromedriver_mac64.zip
    rm chromedriver_mac64.zip
    ;;

  linux*)
    wget http://chromedriver.storage.googleapis.com/2.24/chromedriver_linux64.zip
    unzip -o chromedriver_linux64.zip
    rm chromedriver_linux64.zip
    ;;

  *)        echo "unknown: $OSTYPE" ;;
esac

cd -
