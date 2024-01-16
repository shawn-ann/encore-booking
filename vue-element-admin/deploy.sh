!#/bin/bash
rm -rf ./dist.zip
npm run build:prod
zip -r ./dist.zip ./dist
scp ./dist.zip root@47.101.10.94:/shawn_workspace
