package com.synnex.shellexecutor.service;

import com.synnex.shellexecutor.dao.ConfigRepository;
import com.synnex.shellexecutor.entity.Config;
import com.synnex.shellexecutor.enums.ConfigKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class ConfigServiceImpl implements ConfigService{

    @Autowired
    private ConfigRepository configRepository;

    @Override
    public List<Config> getConfig(ConfigKey key){
        return configRepository.findByKey(key);
    }

    @Override
    public String getBaseDir() {
        List<Config> configs = getConfig(ConfigKey.BASE_DIR);
        if (CollectionUtils.isEmpty(configs)) {
            throw new RuntimeException("No Base dir configured");
        }
        String baseDir = configs.get(0).getValue();
        return baseDir.endsWith("/") || baseDir.endsWith("\\") ? baseDir.substring(0, baseDir.length() - 2) : baseDir;
    }
}
