/*
 * Copyright 2014 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.huzhurong.test.other.log;

import top.huzhurong.test.common.log.AgentLog;
import top.huzhurong.test.common.log.PLoggerBinder;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author emeroad
 */
public class Slf4jLoggerBinder implements PLoggerBinder {

    private ConcurrentMap<String, AgentLog> loggerCache = new ConcurrentHashMap<String, AgentLog>(256, 0.75f, 128);

    @Override
    public AgentLog getLogger(String name) {

        final AgentLog hitPLogger = loggerCache.get(name);
        if (hitPLogger != null) {
            return hitPLogger;
        }

        org.slf4j.Logger slf4jLogger = LoggerFactory.getLogger(name);

        final Logger slf4jLoggerAdapter = new Logger(slf4jLogger);
        final AgentLog before = loggerCache.putIfAbsent(name, slf4jLoggerAdapter);
        if (before != null) {
            return before;
        }
        return slf4jLoggerAdapter;
    }

    @Override
    public void shutdown() {
        // Maybe we don't need to do this. Unregistering LoggerFactory would be enough.
        loggerCache = null;
    }
}
