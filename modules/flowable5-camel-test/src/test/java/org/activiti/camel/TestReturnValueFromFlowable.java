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

package org.activiti.camel;

import java.util.List;

import org.activiti.spring.impl.test.SpringFlowableTestCase;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.Route;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.test.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Saeid Mirzaei
 */

@ContextConfiguration("classpath:generic-camel-flowable-context.xml")
public class TestReturnValueFromFlowable extends SpringFlowableTestCase {
    @Autowired
    CamelContext camelContext;

    @Autowired
    RuntimeService runtimeService;

    @EndpointInject("mock:result")
    protected MockEndpoint resultEndpoint;

    @Produce("direct:startReturnResultTest")
    protected ProducerTemplate template;

    public void setUp() throws Exception {

        camelContext.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("direct:startReturnResultTest").to("flowable:testResultProcess?var.return.exampleCamelReturnValue").to("mock:result");
            }
        });
    }

    public void tearDown() throws Exception {
        List<Route> routes = camelContext.getRoutes();
        for (Route r : routes) {
            camelContext.getRouteController().stopRoute(r.getId());
            camelContext.removeRoute(r.getId());
        }
    }

    @Deployment
    public void testReturnResultFromNewProcess() throws Exception {
        resultEndpoint.expectedPropertyReceived("exampleCamelReturnValue", "hello world.");
        template.sendBody("hello");
        resultEndpoint.assertIsSatisfied();
    }
}
