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
package org.flowable.engine.impl;

import org.flowable.common.engine.impl.db.SchemaOperationsEngineDropDbCmd;
import org.flowable.common.engine.impl.interceptor.Command;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.impl.util.CommandContextUtil;

/**
 * @author Tom Baeyens
 * @author Joram Barrez
 */
public class SchemaOperationProcessEngineClose implements Command<Void> {

    @Override
    public Void execute(CommandContext commandContext) {
        ProcessEngineConfigurationImpl processEngineConfiguration = CommandContextUtil.getProcessEngineConfiguration(commandContext);
        if (processEngineConfiguration.isUsingRelationalDatabase()) {
            String databaseSchemaUpdate = processEngineConfiguration.getDatabaseSchemaUpdate();
            if (org.flowable.engine.ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE_DROP.equals(databaseSchemaUpdate)) {
                new SchemaOperationsEngineDropDbCmd(processEngineConfiguration.getEngineScopeType()).execute(commandContext);
            }
        }
        return null;
    }
}