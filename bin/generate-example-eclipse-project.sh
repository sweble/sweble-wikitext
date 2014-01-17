#!/bin/bash -eu
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

echo
echo "
>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> generate-example-eclipse-project"

. "$( dirname "$0" )/utils.sh"

PRJ="${1:-}"
if [[ -z "$PRJ" ]] || [[ ! -d "$PRJ" ]]; then
  error_exit "Usage: $0 PROJECT"
fi

import_maven_3

pushd "$PRJ" &>/dev/null

  log_info_sep
  log_info "Generating Example Eclipse Project"
  log_info_sep

  WDIR=$(pwd)
  log_info "Working in: ${WDIR}"

  LOG="${WDIR}/generate-example-archetype.log"
  log_info "Logging to: ${LOG}"

  # Redirect all output to logfile
  exec > >(tee "$LOG")
  exec 2>&1

  TITLE=$( basename "${WDIR}" )
  JARPRJ="${WDIR}/target/generated-sources/${TITLE}-jar"
  ZIP="${WDIR}/target/${TITLE}-eclipse-project.zip"

  rm -rf "$LOG" "$JARPRJ" "$ZIP"

  log_info_sep
  log_info "Verifying"
  log_info_sep

  do_mvn verify

  log_info_sep
  log_info "Assembling project files"
  log_info_sep
  log_info "Zipped project: ${ZIP}"
  echo

  mkdir -p "${JARPRJ}"

  mkdir -p "${JARPRJ}/lib"
  mvn_copy_dependencies "${JARPRJ}/lib"

  pushd "${JARPRJ}" &>/dev/null

    cp ${WDIR}/eclipse-project-files/project .project

    cat ${WDIR}/eclipse-project-files/classpath | sed -e '/<!-- LIBS -->/q' > .classpath

    for lib in lib/*.jar; do
      echo "  <classpathentry kind=\"lib\" path=\"${lib}\"/>" >> .classpath
    done

    cat ${WDIR}/eclipse-project-files/classpath | sed -e '1,/<!-- LIBS -->/d' >> .classpath

    dest="src"
    for i in {main,test}; do
      for j in {java,resources}; do
        src="${WDIR}/src/$i/$j"
        if [[ -d $src ]]; then
          echo "copying: $src -> $dest"
          rsync -a $src/ $dest/
        fi
      done
      dest="test"
    done

  popd &>/dev/null

  pushd "$( dirname "${JARPRJ}" )" &>/dev/null
    zip -r "${ZIP}" "$( basename "${JARPRJ}" )"
  popd &>/dev/null

popd &>/dev/null

echo
echo "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< generate-example-eclipse-project"
echo
