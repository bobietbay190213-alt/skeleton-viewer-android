#!/usr/bin/env bash
# Downloads the MediaPipe Pose Landmarker model into app/src/main/assets/
# Must be run from the project root directory.

set -euo pipefail

MODEL_URL="https://storage.googleapis.com/mediapipe-models/pose_landmarker/pose_landmarker_full/float16/latest/pose_landmarker_full.task"
ASSETS_DIR="app/src/main/assets"
MODEL_FILE="$ASSETS_DIR/pose_landmarker_full.task"

echo "Creating assets directory..."
mkdir -p "$ASSETS_DIR"

if [ -f "$MODEL_FILE" ]; then
    echo "Model already exists at $MODEL_FILE — skipping download."
    exit 0
fi

echo "Downloading MediaPipe Pose Landmarker model..."
if command -v wget &>/dev/null; then
    wget -q --show-progress "$MODEL_URL" -O "$MODEL_FILE"
elif command -v curl &>/dev/null; then
    curl -L --progress-bar "$MODEL_URL" -o "$MODEL_FILE"
else
    echo "ERROR: Neither wget nor curl found. Please install one and retry."
    exit 1
fi

echo "Model downloaded successfully: $(du -sh "$MODEL_FILE")"
echo "Path: $MODEL_FILE"
