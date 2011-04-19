/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.wsf.stack.cxf.client.configuration;

import java.net.URL;
//import java.util.logging.Level;
//import java.util.logging.Logger;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.BusApplicationContext;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.buslifecycle.BusLifeCycleListener;
import org.apache.cxf.buslifecycle.BusLifeCycleManager;
//import org.apache.cxf.common.logging.LogUtils;
//import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * A JBossWS version of @see{org.apache.cxf.bus.spring.SpringBusFactory} that
 * allows for loading a custom BusApplicationContext for integration reasons.
 * 
 * @author alessio.soldano@jboss.com
 * @since 25-May-2010
 *
 */
public class JBossWSSpringBusFactory extends SpringBusFactory
{
//   private static final Logger LOG = LogUtils.getL7dLogger(JBossWSSpringBusFactory.class);

   public JBossWSSpringBusFactory()
   {
      super();
   }

   public JBossWSSpringBusFactory(ApplicationContext context)
   {
      super(context);
   }

   @Override
   public Bus createBus(String cfgFiles[], boolean includeDefaults)
   {
//      try
//      {
//         return finishCreatingBus(createApplicationContext(cfgFiles, includeDefaults));
//      }
//      catch (BeansException ex)
//      {
//         LogUtils.log(LOG, Level.WARNING, "APP_CONTEXT_CREATION_FAILED_MSG", ex, (Object[]) null);
//         throw new RuntimeException(ex);
//      }
      Bus bus = super.createBus(cfgFiles, includeDefaults);
      finalizeBusCreation(bus);
      return bus;
   }

   @Override
   public Bus createBus(URL[] urls, boolean includeDefaults)
   {
//      try
//      {
//         return finishCreatingBus(new JBossWSBusApplicationContext(urls, includeDefaults, getApplicationContext()));
//      }
//      catch (BeansException ex)
//      {
//         LogUtils.log(LOG, Level.WARNING, "APP_CONTEXT_CREATION_FAILED_MSG", ex, (Object[]) null);
//         throw new RuntimeException(ex);
//      }
      Bus bus = super.createBus(urls, includeDefaults);
      finalizeBusCreation(bus);
      return bus;
   }
   
   private static void finalizeBusCreation(Bus bus)
   {
      //TODO!! also set our custom configurer
      BusApplicationContext bac = bus.getExtension(BusApplicationContext.class);
      registerAppContextLifeCycleListener(bus, bac);
   }

//   private Bus finishCreatingBus(BusApplicationContext bac)
//   {
//      final Bus bus = (Bus) bac.getBean(Bus.DEFAULT_BUS_ID);
//
//      bus.setExtension(bac, BusApplicationContext.class);
//
//      possiblySetDefaultBus(bus);
//
//      initializeBus(bus);
//
//      registerAppContextLifeCycleListener(bus, bac);
//      return bus;
//   }
//
//   private BusApplicationContext createApplicationContext(String cfgFiles[], boolean includeDefaults)
//   {
//      try
//      {
//         return new JBossWSBusApplicationContext(cfgFiles, includeDefaults, getApplicationContext());
//      }
//      catch (BeansException ex)
//      {
//         LogUtils.log(LOG, Level.WARNING, "INITIAL_APP_CONTEXT_CREATION_FAILED_MSG", ex, (Object[]) null);
//         ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
//         if (contextLoader != BusApplicationContext.class.getClassLoader())
//         {
//            Thread.currentThread().setContextClassLoader(BusApplicationContext.class.getClassLoader());
//            try
//            {
//               return new JBossWSBusApplicationContext(cfgFiles, includeDefaults, getApplicationContext());
//            }
//            finally
//            {
//               Thread.currentThread().setContextClassLoader(contextLoader);
//            }
//         }
//         else
//         {
//            throw ex;
//         }
//      }
//   }

   static void registerAppContextLifeCycleListener(final Bus bus, final BusApplicationContext bac)
   {
      BusLifeCycleManager lm = bus.getExtension(BusLifeCycleManager.class);
      if (null != lm)
      {
         lm.registerLifeCycleListener(new BusApplicationContextLifeCycleListener(bac));
      }
   }

   static class BusApplicationContextLifeCycleListener implements BusLifeCycleListener
   {
      private BusApplicationContext bac;

      BusApplicationContextLifeCycleListener(BusApplicationContext b)
      {
         bac = b;
      }

      public void initComplete()
      {
      }

      public void preShutdown()
      {
      }

      public void postShutdown()
      {
         bac.close();
      }

   }
}
