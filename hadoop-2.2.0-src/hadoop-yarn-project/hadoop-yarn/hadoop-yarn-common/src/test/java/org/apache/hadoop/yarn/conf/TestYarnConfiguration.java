/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.yarn.conf;

import junit.framework.Assert;

import org.apache.hadoop.yarn.webapp.util.WebAppUtils;
import org.junit.Test;

public class TestYarnConfiguration {

  @Test
  public void testDefaultRMWebUrl() throws Exception {
    YarnConfiguration conf = new YarnConfiguration();
    String rmWebUrl = WebAppUtils.getRMWebAppURLWithScheme(conf);
    // shouldn't have a "/" on the end of the url as all the other uri routinnes
    // specifically add slashes and Jetty doesn't handle double slashes.
    Assert.assertNotSame("RM Web Url is not correct", "http://0.0.0.0:8088",
        rmWebUrl);
  }

  @Test
  public void testRMWebUrlSpecified() throws Exception {
    YarnConfiguration conf = new YarnConfiguration();
    // seems a bit odd but right now we are forcing webapp for RM to be
    // RM_ADDRESS
    // for host and use the port from the RM_WEBAPP_ADDRESS
    conf.set(YarnConfiguration.RM_WEBAPP_ADDRESS, "fortesting:24543");
    conf.set(YarnConfiguration.RM_ADDRESS, "rmtesting:9999");
    String rmWebUrl = WebAppUtils.getRMWebAppURLWithScheme(conf);
    String[] parts = rmWebUrl.split(":");
    Assert.assertEquals("RM Web URL Port is incrrect", 24543,
        Integer.valueOf(parts[parts.length - 1]).intValue());
    Assert.assertNotSame(
        "RM Web Url not resolved correctly. Should not be rmtesting",
        "http://rmtesting:24543", rmWebUrl);
  }
  
  @Test
  public void testContainerElastic() throws Exception {
	  YarnConfiguration conf = new YarnConfiguration();
	  Assert.assertEquals(conf.getBoolean(YarnConfiguration.YARN_CONTAINER_ELASTIC, true), 
			  true);
  }

}
