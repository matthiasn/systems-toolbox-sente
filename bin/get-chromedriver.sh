#!/usr/bin/env bash

cd "$( dirname "${BASH_SOURCE[0]}" )"

rm *.zip

download_path=http://chromedriver.storage.googleapis.com/2.35
darwin_archive=chromedriver_mac64.zip
linux_archive=chromedriver_linux64.zip

case "$OSTYPE" in

  darwin*)
    curl -O -J -L $download_path/$darwin_archive
    unzip -o $darwin_archive
    rm $darwin_archive
    ;;

  linux*)
    curl -O -J -L  $download_path/$linux_archive
    unzip -o $linux_archive
    rm $linux_archive
    ;;

  *)        echo "unknown: $OSTYPE" ;;
esac

cd -
