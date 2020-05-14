package com.synnex.shellexecutor.service;

import com.synnex.shellexecutor.entity.Config;
import com.synnex.shellexecutor.enums.ConfigKey;

import java.util.List;

public interface ConfigService {
    List<Config> getConfig(ConfigKey key);

    String getBaseDir();
}
