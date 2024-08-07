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
package org.flowable.dmn.engine.impl;

import java.util.Map;

import org.flowable.common.engine.api.lock.LockManager;
import org.flowable.common.engine.api.management.TableMetaData;
import org.flowable.common.engine.api.management.TablePageQuery;
import org.flowable.common.engine.api.tenant.ChangeTenantIdBuilder;
import org.flowable.common.engine.impl.cmd.CustomSqlExecution;
import org.flowable.common.engine.impl.cmd.GetTableCountCmd;
import org.flowable.common.engine.impl.cmd.GetTableMetaDataCmd;
import org.flowable.common.engine.impl.lock.LockManagerImpl;
import org.flowable.common.engine.impl.persistence.entity.TablePageQueryImpl;
import org.flowable.common.engine.impl.service.CommonEngineServiceImpl;
import org.flowable.common.engine.impl.tenant.ChangeTenantIdBuilderImpl;
import org.flowable.dmn.api.DmnManagementService;
import org.flowable.dmn.engine.DmnEngineConfiguration;
import org.flowable.dmn.engine.impl.cmd.ExecuteCustomSqlCmd;
import org.flowable.dmn.engine.impl.cmd.GetTableNameCmd;

/**
 * @author Tijs Rademakers
 */
public class DmnManagementServiceImpl extends CommonEngineServiceImpl<DmnEngineConfiguration> implements DmnManagementService {

    public DmnManagementServiceImpl(DmnEngineConfiguration dmnEngineConfiguration) {
        super(dmnEngineConfiguration);
    }

    @Override
    public Map<String, Long> getTableCount() {
        return commandExecutor.execute(new GetTableCountCmd(configuration.getEngineCfgKey()));
    }

    @Override
    public String getTableName(Class<?> entityClass) {
        return commandExecutor.execute(new GetTableNameCmd(entityClass));
    }

    @Override
    public TableMetaData getTableMetaData(String tableName) {
        return commandExecutor.execute(new GetTableMetaDataCmd(tableName, configuration.getEngineCfgKey()));
    }

    @Override
    public TablePageQuery createTablePageQuery() {
        return new TablePageQueryImpl(commandExecutor, configuration);
    }

    public <MapperType, ResultType> ResultType executeCustomSql(CustomSqlExecution<MapperType, ResultType> customSqlExecution) {
        Class<MapperType> mapperClass = customSqlExecution.getMapperClass();
        return commandExecutor.execute(new ExecuteCustomSqlCmd<>(mapperClass, customSqlExecution));
    }

    @Override
    public ChangeTenantIdBuilder createChangeTenantIdBuilder(String fromTenantId, String toTenantId) {
        return new ChangeTenantIdBuilderImpl(fromTenantId, toTenantId, configuration.getChangeTenantIdManager());
    }
    
    @Override
    public LockManager getLockManager(String lockName) {
        return new LockManagerImpl(commandExecutor, lockName, getConfiguration().getLockPollRate(), configuration.getEngineCfgKey());
    }

}
