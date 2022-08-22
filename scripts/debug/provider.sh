#!/bin/bash

./gradlew :dubbo-provider:bootRun --debug-jvm \
--add-opens java.base/sun.net.util=ALL-UNNAMED \
--add-opens java.base/java.lang=ALL-UNNAMED \
--add-opens java.base/java.math=ALL-UNNAMED
