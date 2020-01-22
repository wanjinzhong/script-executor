### Script Executor
1. Prepare script script at `/home/neilw/script-executor/script`
2. Add the config script at `/home/neilw/script-executor/config.json` .The config file like:
```json
[
  {
    "id": 1,
    "name": "PLM2 E2E Restart",
    "desc": "Restart the local PLM2 E2E environment manually",
    "script": "restart-plm2-e2e.sh",
    "successMsg": "Message alter when call script successfully"
  }
]
```