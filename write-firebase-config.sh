#!/bin/bash

# Write the overall firebase config:

jq -n \
  --arg FIREBASE_PROJECT_ID "$FIREBASE_PROJECT_ID" \
  -f .firebaserc.jq \
  > .firebaserc

# Write the json file loaded by the angular build:

jq -n \
  --arg FIREBASE_PROJECT_ID "$FIREBASE_PROJECT_ID" \
  --arg FIREBASE_APP_ID "$FIREBASE_APP_ID" \
  --arg FIREBASE_STORAGE_BUCKET "$FIREBASE_STORAGE_BUCKET" \
  --arg FIREBASE_API_KEY "$FIREBASE_API_KEY" \
  --arg FIREBASE_AUTH_DOMAIN "$FIREBASE_AUTH_DOMAIN" \
  --arg FIREBASE_MESSAGING_SENDER_ID "$FIREBASE_MESSAGING_SENDER_ID" \
  --arg FIREBASE_USE_EMULATORS "$FIREBASE_USE_EMULATORS" \
  -f webApp/src/jsMain/resources/firebase-config.json.jq \
  > webApp/src/jsMain/resources/firebase-config.json
