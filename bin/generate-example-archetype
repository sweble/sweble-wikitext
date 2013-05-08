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
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> generate-example-archetype"

. "$( dirname "$0" )/utils.sh"

PRJ="${1:-}"
if [[ -z "$PRJ" ]] || [[ ! -d "$PRJ" ]]; then
  error_exit "Usage: $0 PROJECT"
fi

import_maven_3

# Important for platform independant sort!
export LC_COLLATE=POSIX

echo
pushd "$PRJ" &>/dev/null

  log_info_sep
  log_info "Generating Example Archetype"
  log_info_sep

  WDIR=$(pwd)
  log_info "Working in: ${WDIR}"

  LOG="${WDIR}/generate-example-archetype.log"
  log_info "Logging to: ${LOG}"

  # Redirect all output to logfile
  exec > >(tee "$LOG")
  exec 2>&1

  log_info
  log_info "Gathering information on sweble-wikitext parent pom..."
  pushd ../ &>/dev/null
    SWEBLE_WIKITEXT_VERSION=$( mvn_get_pom_version )
    SWEBLE_WIKITEXT_GROUP_ID=$( mvn_get_pom_group_id )
    SWEBLE_WIKITEXT_ARTIFACT_ID=$( mvn_get_pom_artifact_id )
  popd &>/dev/null
  if [[ $SWEBLE_WIKITEXT_GROUP_ID != "org.sweble.wikitext" ]] || [[ $SWEBLE_WIKITEXT_ARTIFACT_ID != "sweble-wikitext" ]]; then
    error_exit "Expected org.sweble.wikitext:sweble-wikitext project in parent directory!"
  fi
  log_info "  Parent project version: ${SWEBLE_WIKITEXT_VERSION}"

  log_info
  log_info "Gathering information on example project ..."
  PRJ_VERSION=$( mvn_get_pom_version )
  PRJ_GROUP_ID=$( mvn_get_pom_group_id )
  PRJ_ARTIFACT_ID=$( mvn_get_pom_artifact_id )
  log_info "  Project groupId: ${PRJ_GROUP_ID}"
  log_info "  Project artifactId: ${PRJ_ARTIFACT_ID}"
  log_info "  Project version: ${PRJ_VERSION}"

  ARCHETYPE_SRC_DIR="${WDIR}/target/generated-sources/archetype-project"
  ARCHETYPE_DST_DIR="${WDIR}/../${PRJ_ARTIFACT_ID}-archetype"
  ARCHETYPE_TEST_DIR="${WDIR}/target/test-archetype"

  # Build example project and generate archetype
  rm -rf "$LOG" "$ARCHETYPE_SRC_DIR" "$ARCHETYPE_DST_DIR" "$ARCHETYPE_TEST_DIR"

  log_info
  log_info "Gathering sources ..."
  mkdir -p "$ARCHETYPE_SRC_DIR"
  cp -a LICENSE NOTICE README pom.xml src/ "$ARCHETYPE_SRC_DIR"

  log_info
  log_info_sep
  log_info "Generating archetype"
  log_info_sep
  log_info "Archetype location: ${ARCHETYPE_DST_DIR}"
  
  pushd "$ARCHETYPE_SRC_DIR" &>/dev/null

    do_mvn archetype:create-from-project
    mv  target/generated-sources/archetype "$ARCHETYPE_DST_DIR"

  popd &>/dev/null

  # Use our own, beautifully formatted pom.xml file
  cp -a archetype-files/* ${ARCHETYPE_DST_DIR}/

  # Fix archetype's pom.xml
  cat archetype-files/pom.xml \
    | sed -e "s/\${ARCHETYPE_VERSION}/${PRJ_VERSION}/" \
    | sed -e "s/\${SWEBLE_WIKITEXT_VERSION}/${SWEBLE_WIKITEXT_VERSION}/" \
    > ${ARCHETYPE_DST_DIR}/pom.xml

  # Don't know what we would need that for:
  rm -rf ${ARCHETYPE_DST_DIR}/{src/test/,target/}

  # Check contents
  EXPECTED_FILES=$(cat archetype-content.txt)
  ACTUAL_FILES=$(find ${ARCHETYPE_DST_DIR} | sort)
  ACTUAL_FILES=${ACTUAL_FILES//$ARCHETYPE_DST_DIR/}

  if [ ! "${ACTUAL_FILES}" = "${EXPECTED_FILES}" ]; then
    AF=`mktemp`
    echo "${ACTUAL_FILES}" > "${AF}"
    diff archetype-content.txt "${AF}" || true
    rm "${AF}"
    echo
    error_exit "Unexpected files in ${ARCHETYPE_DST_DIR}!"
  fi

  REQPROP=$(cat <<EOF
  <requiredProperties>
    <requiredProperty key="groupId">
      <defaultValue>${PRJ_GROUP_ID}</defaultValue>
    </requiredProperty>
    <requiredProperty key="artifactId">
      <defaultValue>${PRJ_ARTIFACT_ID}</defaultValue>
    </requiredProperty>
  </requiredProperties>
</archetype-descriptor>
EOF
)

  METADATA="${ARCHETYPE_DST_DIR}/src/main/resources/META-INF/maven/archetype-metadata.xml"
  cat "${METADATA}" | grep -v '</archetype-descriptor>' > "${METADATA}.tmp"
  echo "${REQPROP}" >> "${METADATA}.tmp"
  mv "${METADATA}.tmp" "${METADATA}"

  # Install archetype/Update local catalog
  pushd ${ARCHETYPE_DST_DIR} &>/dev/null

    log_info_sep
    log_info "Installing archetype ..."
    log_info_sep

    AT_VERSION=$( mvn_get_pom_version )
    AT_GROUP_ID=$( mvn_get_pom_group_id )
    AT_ARTIFACT_ID=$( mvn_get_pom_artifact_id )

    log_info "Archetype groupId: ${AT_GROUP_ID}"
    log_info "Archetype artifactId: ${AT_ARTIFACT_ID}"
    log_info "Archetype version: ${AT_VERSION}"
    log_info
    log_info "Jar location: ${ARCHETYPE_DST_DIR}/target/${AT_ARTIFACT_ID}-${AT_VERSION}.jar"

    do_mvn install

    log_info_sep
    log_info "Generating site"
    log_info_sep
    log_info "Site location: ${ARCHETYPE_DST_DIR}/target/staging"

    do_mvn site site:stage

  popd &>/dev/null

  log_info_sep
  log_info "Generating project from archetype"
  log_info_sep

  PROJECT=project
  rm -rf ${ARCHETYPE_TEST_DIR}
  mkdir ${ARCHETYPE_TEST_DIR}
  pushd ${ARCHETYPE_TEST_DIR} &>/dev/null
  do_mvn archetype:generate \
    -B \
    -DarchetypeCatalog=local \
    -DarchetypeGroupId=${AT_GROUP_ID} \
    -DarchetypeArtifactId=${AT_ARTIFACT_ID} \
    -DarchetypeVersion=${AT_VERSION} \
    -DgroupId=org.example \
    -DartifactId=${PROJECT} \
    -Dversion=1.0
  popd &>/dev/null

  log_info_sep
  log_info "Verifying generated project"
  log_info_sep

  pushd ${ARCHETYPE_TEST_DIR}/${PROJECT} &>/dev/null
  do_mvn verify
  popd &>/dev/null

  # Deploy
  if [[ ! -z ${DEPLOY_EXAMPLE_ARCHETYPES-} ]]; then

    pushd ${ARCHETYPE_DST_DIR} &>/dev/null

      log_info_sep
      log_info "Deploying archetype"
      log_info_sep

      do_mvn deploy $DEPLOY_EXAMPLE_ARCHETYPES

    popd &>/dev/null

  fi

popd &>/dev/null

echo "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< generate-example-archetype"
echo
