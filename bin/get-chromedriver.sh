#!/usr/bin/env bash

cd "$( dirname "${BASH_SOURCE[0]}" )"

case "$OSTYPE" in

  darwin*)
    wget http://chromedriver.storage.googleapis.com/2.20/chromedriver_mac32.zip
    unzip -o chromedriver_mac32.zip
    rm chromedriver_mac32.zip
    ;;

  linux*)
    wget http://chromedriver.storage.googleapis.com/2.20/chromedriver_linux64.zip
    unzip -o chromedriver_linux64.zip
    rm chromedriver_linux64.zip
    ;;

  *)        echo "unknown: $OSTYPE" ;;
esac

cd -
