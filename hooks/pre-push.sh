#!/bin/sh
# this hook is in SCM so that it can be shared
# to install it, create a symbolic link in the projects .git/hooks folder
#
#       i.e. - from the .git/hooks directory, run
#               $ ln -s ../../git-hooks/pre-commit.sh pre-commit
#
# to skip the tests, run with the --no-verify argument
#       i.e. - $ 'git commit --no-verify'

# stash any unstaged changes
echo "Running test..."

./gradlew app:check --daemon

status=$?

# return 1 exit code if running checks fails
[ $status -ne 0 ] && exit 1
exit 0
