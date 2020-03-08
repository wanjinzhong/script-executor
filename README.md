### Script Executor
1. Prepare script script at `~/script-executor/script` (You can also change this directory in `application.yml`)
2. Add the config script at `~/script-executor/config.json` .The config file like:
```json
[
  {
    "id": 1,
    "name": "PLM2 E2E Restart",
    "seq": 1,
    "group": "PLM2",
    "desc": "Restart the local PLM2 E2E environment manually",
    "script": "restart-plm2-e2e.sh",
    "successMsg": "Message alter when call script successfully"
  }
]
```