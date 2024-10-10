package com.pabu5h.comtrade.processor;

import com.pabu5h.comtrade.ComtradeModule;
import com.pabu5h.comtrade.config.ComtradeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Service
public class ComtradeProcessor {
    @Autowired
    private ComtradeModule comtradeModule;
    public Map<String, Object> processPqd(InputStream cfgInputStream, InputStream datInputStream) throws IOException {
        Map<String, Object> result = comtradeModule.processCfgConfig(cfgInputStream, datInputStream);
        ComtradeConfig comtradeResult = (ComtradeConfig) result.get("result");
        if (comtradeResult != null) {
            result = comtradeModule.convertConfigFieldsToMap(comtradeResult);
        }
        return result;
    }
}
