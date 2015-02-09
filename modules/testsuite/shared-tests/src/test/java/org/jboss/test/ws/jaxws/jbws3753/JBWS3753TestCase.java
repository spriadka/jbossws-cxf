/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.test.ws.jaxws.jbws3753;

import java.io.File;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.wsf.test.JBossWSTest;
import org.jboss.wsf.test.JBossWSTestHelper;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * [JBWS-3753] Improve destination matching when processing requests
 *
 * @author alessio.soldano@jboss.com
 */
@RunWith(Arquillian.class)
public class JBWS3753TestCase extends JBossWSTest
{
   @ArquillianResource
   private URL baseURL;

   @Deployment(testable = false)
   public static WebArchive createDeployments() {
      WebArchive archive = ShrinkWrap.create(WebArchive.class, "jaxws-jbws3753.war");
         archive
               .addManifest()
               .addClass(org.jboss.test.ws.jaxws.jbws3753.ServiceAImpl.class)
               .addClass(org.jboss.test.ws.jaxws.jbws3753.ServiceBImpl.class)
               .addClass(org.jboss.test.ws.jaxws.jbws3753.ServiceImpl.class)
               .addClass(org.jboss.test.ws.jaxws.jbws3753.ServiceInterface.class)
               .setWebXML(new File(JBossWSTestHelper.getTestResourcesDir() + "/jaxws/jbws3753/WEB-INF/web.xml"));
     return archive;
   }

   @Test
   @RunAsClient
   public void testService() throws Exception
   {
      Service service = Service.create(new URL(baseURL + "/service?wsdl"), new QName("http://org.jboss.ws/jaxws/jbws3753/", "MyService"));
      ServiceInterface port = service.getPort(ServiceInterface.class);
      assertEquals("Hi John", port.greetMe("John"));
   }

   @Test
   @RunAsClient
   public void testServiceA() throws Exception
   {
      Service service = Service.create(new URL(baseURL + "/serviceA?wsdl"), new QName("http://org.jboss.ws/jaxws/jbws3753/", "MyService"));
      ServiceInterface port = service.getPort(ServiceInterface.class);
      assertEquals("(A) Hi John", port.greetMe("John"));
   }

   @Test
   @RunAsClient
   public void testServiceB() throws Exception
   {
      Service service = Service.create(new URL(baseURL + "/serviceB?wsdl"), new QName("http://org.jboss.ws/jaxws/jbws3753/", "MyService"));
      ServiceInterface port = service.getPort(ServiceInterface.class);
      assertEquals("(B) Hi John", port.greetMe("John"));
   }

}
