### Shell Executor
1. Prepare shell script at `/home/neilw/shell-executor/shell`
2. Add the config script at `/home/neilw/shell-executor/config.json` .The config file like:
```json
[
  {
    "name": "PLM2 E2E Restart",
    "desc": "Restart the local PLM2 E2E environment manually",
    "script": "restart-plm2-e2e.sh"
  }
]
```