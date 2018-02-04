#!/usr/bin/env bash

cd "$( dirname "${BASH_SOURCE[0]}" )"

rm *.tar.gz

download_path=https://github.com/mozilla/geckodriver/releases/download/v0.19.1
darwin_archive=geckodriver-v0.19.1-macos.tar.gz
linux_archive=geckodriver-v0.19.1-macos.tar.gz

case "$OSTYPE" in

  darwin*)
    curl -O -J -L $download_path/$darwin_archive
    tar -xzf $darwin_archive
    rm $darwin_archive
    ;;

  linux*)
    curl -O -J -L  $download_path/$linux_archive
    tar -xzf $linux_archive
    rm $linux_archive
    ;;

  *)        echo "unknown: $OSTYPE" ;;
esac

cd -
