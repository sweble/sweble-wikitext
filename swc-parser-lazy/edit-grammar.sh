#!/bin/bash
#
# Copyright 2011 The Open Source Research Group,
#                University of Erlangen-Nürnberg
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

#
# Usage:
# ------
#
# ./edit-grammar.sh
#
# The script must be run in the ptk-nodegen-maven-plugin directory.
# The plugin requires the programs bash, screen and kate.
#

BIN=`which kate`

OPTIONS=`${BIN} -v | grep "KDE 4" &> /dev/null && echo "--startanon"`

GRAMMAR="src/main/autogen/org/sweble/wikitext/parser"

find ${GRAMMAR} -name "*.rats" | xargs screen -d -m ${BIN} ${OPTIONS}
