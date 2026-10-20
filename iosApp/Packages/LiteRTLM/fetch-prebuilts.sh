#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
PREBUILT="$ROOT/Prebuilt"
VERSION="v0.18.0"
BASE="https://github.com/google-ai-edge/LiteRT-LM/releases/download/${VERSION}"

mkdir -p "$PREBUILT"
TMP="$(mktemp -d)"
cleanup() { rm -rf "$TMP"; }
trap cleanup EXIT

download() {
  local name="$1"
  local checksum="$2"
  local zip="$TMP/${name}.xcframework.zip"
  curl -L --http1.1 --retry 5 --retry-delay 2 -o "$zip" "${BASE}/${name}.xcframework.zip"
  echo "${checksum}  ${zip}" | shasum -a 256 -c -
  rm -rf "${PREBUILT}/${name}.xcframework"
  unzip -qo "$zip" -d "$PREBUILT"
}

download CLiteRTLM d765b99592d4ec3d0c9e2bd69469454af06c834861340672da1891c0c121c347
download CLiteRTLM_mac 5f6ee68d95eeccb084c6e66d5ee47255e3020fa0fb29696dd0301ae26d6cfb4f

echo "LiteRT-LM ${VERSION} prebuilts ready in ${PREBUILT}"
