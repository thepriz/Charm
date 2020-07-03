#!/usr/bin/env bash

if [ -z `which sed` ]; then
  echo "Missing sed"
  exit 1
fi

if [ -z "$1" ]; then
  echo "Missing type"
  exit 1
fi

ASSETS="../src/main/resources/assets/charm"
A=(${1//:/ })

if [ -z "${A[1]}" ]; then
  NAMESPACE="minecraft"
  TYPE="${A[0]}"
else
  NAMESPACE="${A[0]}"
  TYPE="${A[1]}"
fi

copy_replace() {
  SRC=$1
  DEST=$2

  cp "${SRC}" "${DEST}"
  sed -i "s/TYPE/${TYPE}/g" "${DEST}"
  sed -i "s/NAMESPACE/${NAMESPACE}/g" "${DEST}"
}

# barrels
copy_replace "barrel_blockstate" "${ASSETS}/blockstates/${TYPE}_barrel.json"
copy_replace "barrel_item_model" "${ASSETS}/models/item/${TYPE}_barrel.json"
copy_replace "barrel_block_model" "${ASSETS}/models/block/${TYPE}_barrel.json"
copy_replace "barrel_open_block_model" "${ASSETS}/models/block/${TYPE}_barrel_open.json"

# bookshelf chests
copy_replace "bookshelf_chest_blockstate" "${ASSETS}/blockstates/${TYPE}_bookshelf_chest.json"
copy_replace "bookshelf_chest_0_item_model" "${ASSETS}/models/item/${TYPE}_bookshelf_chest_0.json"
copy_replace "bookshelf_chest_0_block_model" "${ASSETS}/models/block/${TYPE}_bookshelf_chest_0.json"
for i in {1..9}
do
  copy_replace "bookshelf_chest_x_block_model" "${ASSETS}/models/block/${TYPE}_bookshelf_chest_${i}.json"
done
