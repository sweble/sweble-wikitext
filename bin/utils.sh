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
# == Error handling ============================================================
#

function error_exit
{
    echo 1>&2
    echo "${1:-"Unknown Error"}" 1>&2
    echo 1>&2
    exit 1
}

function dump_output_on_error_and_exit() {
  echo 1>&2
  echo 1>&2
  echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" 1>&2
  echo "$1" 1>&2
  echo "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" 1>&2
  echo "Working directory: $( pwd )" 1>&2
  error_exit "$2"
}

#
# == Environment ===============================================================
#

function is_maven_3
{
  "$1" -v 2>&1 | grep "Apache Maven 3" &>/dev/null
}

function import_maven_3
{
  if [[ ${MVN:-} == "" ]]; then
    if ! MVN=$( which mvn ) || ! is_maven_3 "$MVN"; then
      MVN=`which mvn3` \
        || error_exit "$BASH_SOURCE:$LINENO: Maven 3 executable not found"
    fi
  fi
}

function import_git
{
  if [[ ${GIT:-} == "" ]]; then
    GIT=$( which git ) \
      || error_exit "$BASH_SOURCE:$LINENO: Git executable not found"
  fi
}

#
# == Helpers ===================================================================
#

function format_now() {
  date '+%Y%m%d-%H%M'
}

function log_info_sep() {
  echo "[INFO] ------------------------------------------------------------------------"
}

function log_info() {
  if [[ $# == 0 ]]; then
    echo "[INFO]"
  else
    for i in "$@"; do
      echo "[INFO] $i"
    done
  fi
}

#
# == Maven Helper ==============================================================
#

function mvn_get_pom_variable() {
  VARIABLE=$1
  SUBJECT=$2
  EXTRACT=$3

  set +e
  CMD="$MVN help:evaluate -Dexpression=\"$VARIABLE\""
  echo $( pwd ) 1>&2
  echo "+ $CMD" 1>&2
  OUTPUT=$( eval $CMD 2>&1 )
  RESULT=$?
  set -e
  if [[ $RESULT != 0 ]]; then
    dump_output_on_error_and_exit "$OUTPUT" "Command failed: $CMD"
  fi

  set +e
  ANSWER=$( echo "$OUTPUT" | eval "$EXTRACT" )
  RESULT=$?
  set -e
  if [[ $RESULT != 0 || $ANSWER == "" ]]; then
    dump_output_on_error_and_exit "$OUTPUT" "Failed to retrieve $SUBJECT in above output!"
  fi

  echo "$ANSWER"
}

function do_mvn() {
  echo
  echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> MVN"
  echo $( pwd ) 1>&2
  ( set -vx
      eval $MVN "$@"
  )
  echo "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< MVN"
  echo
}

function mvn_get_pom_version() {
  mvn_get_pom_variable \
    "project.version" \
    "maven project version" \
    "grep -e '^[0-9]\+\.[0-9]\+\.[0-9]\+\(-[A-Za-z0-9_]\+\(-[0-9]\+\)\?\)\?\(-[A-Za-z0-9_]\+\)\?$'"
}

function mvn_get_pom_artifact_id() {
  mvn_get_pom_variable \
    "project.artifactId" \
    "maven project artifact id" \
    "grep -e '^[a-zA-Z_-]\+$' | head -n1"
}

function mvn_get_pom_group_id() {
  mvn_get_pom_variable \
    "project.groupId" \
    "maven project group id" \
    "grep -e '^[a-zA-Z_]\+\(\.[a-zA-Z_]\+\)*$' | head -n1"
}

function mvn_get_dependency_version() {
  GROUPID="$1"
  ARTIFACTID="$2"

  set +e
  CMD="$MVN -N dependency:list"
  echo $( pwd ) 1>&2
  echo "+ $CMD" 1>&2
  OUTPUT=$( eval $CMD 2>&1 )
  RESULT=$?
  set -e
  if [[ $RESULT != 0 ]]; then
    dump_output_on_error_and_exit "$OUTPUT" "Command failed: $CMD"
  fi

  set +e
  ANSWER=$( echo "$OUTPUT" | \
    grep -e "\[INFO\][ ]*${GROUPID}:${ARTIFACTID}:jar:[^:]\+:" | \
    sed -e "s/.*${GROUPID}:${ARTIFACTID}:jar:\([^:]\+\):.*/\1/" )
  RESULT=$?
  set -e
  if [[ $RESULT != 0 || $ANSWER == "" ]]; then
    dump_output_on_error_and_exit "$OUTPUT" "Failed to retrieve dependency version in above output for: ${GROUPID}:${ARTIFACTID}!"
  fi

  echo "$ANSWER"
}

function mvn_download_artifact() {
  GROUPID="$1"
  ARTIFACTID="$2"
  VERSION="$3"
  CLASSIFIER="$4"
  TARGETDIR="$5"

  JAR="${ARTIFACTID}-${VERSION}-${CLASSIFIER}.jar"

  set +e
  CMD="$MVN org.apache.maven.plugins:maven-dependency-plugin:2.4:get
    -DgroupId=${GROUPID}
    -DartifactId=${ARTIFACTID}
    -Dversion=${VERSION}
    -Dpackaging=jar
    -Dclassifier=${CLASSIFIER}
    -Ddest=\"${TARGETDIR}/${JAR}\""
  echo $( pwd ) 1>&2
  echo "+ $CMD" 1>&2
  OUTPUT=$( eval $CMD 2>&1 )
  RESULT=$?
  set -e
  if [[ $RESULT != 0 || ! -f "${TARGETDIR}/${JAR}" ]]; then
    dump_output_on_error_and_exit "$OUTPUT" "Command failed: $CMD"
  fi

  echo "$JAR"
}

function mvn_copy_dependencies() {
  TARGETDIR="$1"

  set +e
  CMD="$MVN org.apache.maven.plugins:maven-dependency-plugin:2.4:copy-dependencies \
    -DoutputDirectory=\"${TARGETDIR}\""
  echo $( pwd ) 1>&2
  echo "+ $CMD" 1>&2
  OUTPUT=$( eval $CMD 2>&1 )
  RESULT=$?
  set -e
  if [[ $RESULT != 0 || ! -d "${TARGETDIR}" ]]; then
    dump_output_on_error_and_exit "$OUTPUT" "Command failed: $CMD"
  fi
}
