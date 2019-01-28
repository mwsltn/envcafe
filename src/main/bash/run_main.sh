#!/bin/bash

echo "Running EnvCafe Example/Test"
java -jar $(find ../../../target -maxdepth 1 -name "*.jar")