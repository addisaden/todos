#!/bin/sh

echo "Push to all remote repos."

echo

export currentBranch = $(git branch)

for server in $(git remote)
do
  echo "Upload to $server on $currentBranch"
  echo
  git push $server $currentBranch
done

echo

git branch -va
