#!/bin/sh

echo "Push to all remote repos."

echo

currentBranch=$(git branch | cut -d \  -f 2)

for server in $(git remote)
do
  echo
  echo "Upload to $server on $currentBranch"
  echo
  git push $server $currentBranch
done

echo

git branch -va
