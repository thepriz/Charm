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
  IT=$3

  cp "${SRC}" "${DEST}"
  sed -i "s/TYPE/${TYPE}/g" "${DEST}"
  sed -i "s/NAMESPACE/${NAMESPACE}/g" "${DEST}"

  if [ -n "${IT}" ]; then
    sed -i "s/?/${IT}/g" "${DEST}"
  fi
}

add_lang_strings() {
  NAME="$(tr '[:lower:]' '[:upper:]' <<< ${TYPE:0:1})${TYPE:1}"
  LANGFILE="${ASSETS}/lang/en_us.json"
  sed -i ':a;N;$!ba;s/"\n/",\n/g' "${LANGFILE}" # add a comma after the last entry
  sed -i 's/}//g' "${LANGFILE}" # remove the closing brace

  # add new lang entries
  {
    echo "  \"block.charm.${TYPE}_barrel\": \"${NAME} Barrel\",";
    echo "  \"block.charm.${TYPE}_bookshelf_chest\": \"${NAME} Bookshelf Chest\","
    echo "  \"block.charm.${TYPE}_crate_open\": \"${NAME} Crate\",";
    echo "  \"block.charm.${TYPE}_crate_sealed\": \"${NAME} Sealed Crate\""
    echo "}"
  } >> $LANGFILE
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
  copy_replace "bookshelf_chest_x_block_model" "${ASSETS}/models/block/${TYPE}_bookshelf_chest_${i}.json" $i
done

# crates
copy_replace "crate_open_blockstate" "${ASSETS}/blockstates/${TYPE}_crate_open.json"
copy_replace "crate_open_block_model" "${ASSETS}/models/block/${TYPE}_crate_open.json"
copy_replace "crate_open_item_model" "${ASSETS}/models/item/${TYPE}_crate_open.json"
copy_replace "crate_sealed_blockstate" "${ASSETS}/blockstates/${TYPE}_crate_sealed.json"
copy_replace "crate_sealed_block_model" "${ASSETS}/models/block/${TYPE}_crate_sealed.json"
copy_replace "crate_sealed_item_model" "${ASSETS}/models/item/${TYPE}_crate_sealed.json"

add_lang_strings