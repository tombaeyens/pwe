#!/bin/bash

java -cp $ANTLR_HOME/antlr-$ANTLR_VERSION-complete.jar org.antlr.v4.Tool -o ../java/ch03/data/expression/parser/input -package ch03.data.expression.parser.input InputExpression.g4
