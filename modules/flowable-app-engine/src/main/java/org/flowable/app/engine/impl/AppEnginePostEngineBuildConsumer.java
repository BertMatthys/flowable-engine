/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flowable.app.engine.impl;

import java.util.function.Consumer;

import org.flowable.app.engine.AppEngine;
import org.flowable.app.engine.AppEngineConfiguration;
import org.flowable.common.engine.api.engine.EngineLifecycleListener;

/**
 * @author Filip Hrisafov
 */
public class AppEnginePostEngineBuildConsumer implements Consumer<AppEngine> {

    @Override
    public void accept(AppEngine appEngine) {
        AppEngineConfiguration engineConfiguration = appEngine.getAppEngineConfiguration();
        if (engineConfiguration.getEngineLifecycleListeners() != null) {
            for (EngineLifecycleListener engineLifecycleListener : engineConfiguration.getEngineLifecycleListeners()) {
                engineLifecycleListener.onEngineBuilt(appEngine);
            }
        }
    }
}
