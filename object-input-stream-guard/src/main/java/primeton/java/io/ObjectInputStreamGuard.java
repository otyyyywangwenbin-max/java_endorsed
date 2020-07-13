/*******************************************************************************
 * $Header$
 * $Revision$
 * $Date$
 *
 *==============================================================================
 *
 * Copyright (c) 2001-2017 Primeton Technologies, Ltd.
 * All rights reserved.
 * 
 * Created on Jul 13, 2020
 *******************************************************************************/

package primeton.java.io;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO 此处填写 class 信息
 *
 * @author wangwb (mailto:wangwb@primeton.com)
 */

public class ObjectInputStreamGuard {

    protected final static String PREFIX_SPRING = "org.springframework.";

    protected final static String PREFIX_C3P0 = "com.mchange.v2.c3p0.";

    /**
     * Set of well-known "nasty classes", deserialization of which is considered
     * dangerous and should (and is) prevented by default.
     */
    protected final static Set<String> DEFAULT_NO_DESER_CLASS_NAMES;
    static {
        Set<String> s = new HashSet<String>();
        // Courtesy of [https://github.com/kantega/notsoserial]:
        // (and wrt [databind#1599])
        s.add("org.apache.commons.collections.functors.InvokerTransformer");
        s.add("org.apache.commons.collections.functors.InstantiateTransformer");
        s.add("org.apache.commons.collections4.functors.InvokerTransformer");
        s.add("org.apache.commons.collections4.functors.InstantiateTransformer");
        s.add("org.codehaus.groovy.runtime.ConvertedClosure");
        s.add("org.codehaus.groovy.runtime.MethodClosure");
        s.add("org.springframework.beans.factory.ObjectFactory");
        s.add("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
        s.add("org.apache.xalan.xsltc.trax.TemplatesImpl");
        // [databind#1680]: may or may not be problem, take no chance
        s.add("com.sun.rowset.JdbcRowSetImpl");
        // [databind#1737]; JDK provided
        s.add("java.util.logging.FileHandler");
        s.add("java.rmi.server.UnicastRemoteObject");
        // [databind#1737]; 3rd party
//s.add("org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor"); // deprecated by [databind#1855]
        s.add("org.springframework.beans.factory.config.PropertyPathFactoryBean");
        // [databind#2680]
        s.add("org.springframework.aop.config.MethodLocatingFactoryBean");
        s.add("org.springframework.beans.factory.config.BeanReferenceFactoryBean");

// s.add("com.mchange.v2.c3p0.JndiRefForwardingDataSource"); // deprecated by [databind#1931]
// s.add("com.mchange.v2.c3p0.WrapperConnectionPoolDataSource"); // - "" -
        // [databind#1855]: more 3rd party
        s.add("org.apache.tomcat.dbcp.dbcp2.BasicDataSource");
        s.add("com.sun.org.apache.bcel.internal.util.ClassLoader");
        // [databind#1899]: more 3rd party
        s.add("org.hibernate.jmx.StatisticsService");
        s.add("org.apache.ibatis.datasource.jndi.JndiDataSourceFactory");
        // [databind#2032]: more 3rd party; data exfiltration via xml parsed ext
        // entities
        s.add("org.apache.ibatis.parsing.XPathParser");

        // [databind#2052]: Jodd-db, with jndi/ldap lookup
        s.add("jodd.db.connection.DataSourceConnectionProvider");

        // [databind#2058]: Oracle JDBC driver, with jndi/ldap lookup
        s.add("oracle.jdbc.connector.OracleManagedConnectionFactory");
        s.add("oracle.jdbc.rowset.OracleJDBCRowSet");

        // [databind#2097]: some 3rd party, one JDK-bundled
        s.add("org.slf4j.ext.EventData");
        s.add("flex.messaging.util.concurrent.AsynchBeansWorkManagerExecutor");
        s.add("com.sun.deploy.security.ruleset.DRSHelper");
        s.add("org.apache.axis2.jaxws.spi.handler.HandlerResolverImpl");

        // [databind#2186], [databind#2670]: yet more 3rd party gadgets
        s.add("org.jboss.util.propertyeditor.DocumentEditor");
        s.add("org.apache.openjpa.ee.RegistryManagedRuntime");
        s.add("org.apache.openjpa.ee.JNDIManagedRuntime");
        s.add("org.apache.openjpa.ee.WASRegistryManagedRuntime"); // [#2670]
                                                                  // addition
        s.add("org.apache.axis2.transport.jms.JMSOutTransportInfo");

        // [databind#2326] (2.9.9)
        s.add("com.mysql.cj.jdbc.admin.MiniAdmin");

        // [databind#2334]: logback-core (2.9.9.1)
        s.add("ch.qos.logback.core.db.DriverManagerConnectionSource");

        // [databind#2341]: jdom/jdom2 (2.9.9.1)
        s.add("org.jdom.transform.XSLTransformer");
        s.add("org.jdom2.transform.XSLTransformer");

        // [databind#2387], [databind#2460]: EHCache
        s.add("net.sf.ehcache.transaction.manager.DefaultTransactionManagerLookup");
        s.add("net.sf.ehcache.hibernate.EhcacheJtaTransactionManagerLookup");

        // [databind#2389]: logback/jndi
        s.add("ch.qos.logback.core.db.JNDIConnectionSource");

        // [databind#2410]: HikariCP/metricRegistry config
        s.add("com.zaxxer.hikari.HikariConfig");
        // [databind#2449]: and sub-class thereof
        s.add("com.zaxxer.hikari.HikariDataSource");

        // [databind#2420]: CXF/JAX-RS provider/XSLT
        s.add("org.apache.cxf.jaxrs.provider.XSLTJaxbProvider");

        // [databind#2462]: commons-configuration / -2
        s.add("org.apache.commons.configuration.JNDIConfiguration");
        s.add("org.apache.commons.configuration2.JNDIConfiguration");

        // [databind#2469]: xalan
        s.add("org.apache.xalan.lib.sql.JNDIConnectionPool");
        // [databind#2704]: xalan2
        s.add("com.sun.org.apache.xalan.internal.lib.sql.JNDIConnectionPool");

        // [databind#2478]: comons-dbcp, p6spy
        s.add("org.apache.commons.dbcp.datasources.PerUserPoolDataSource");
        s.add("org.apache.commons.dbcp.datasources.SharedPoolDataSource");
        s.add("com.p6spy.engine.spy.P6DataSource");

        // [databind#2498]: log4j-extras (1.2)
        s.add("org.apache.log4j.receivers.db.DriverManagerConnectionSource");
        s.add("org.apache.log4j.receivers.db.JNDIConnectionSource");

        // [databind#2526]: some more ehcache
        s.add("net.sf.ehcache.transaction.manager.selector.GenericJndiSelector");
        s.add("net.sf.ehcache.transaction.manager.selector.GlassfishSelector");

        // [databind#2620]: xbean-reflect
        s.add("org.apache.xbean.propertyeditor.JndiConverter");

        // [databind#2631]: shaded hikari-config
        s.add("org.apache.hadoop.shaded.com.zaxxer.hikari.HikariConfig");

        // [databind#2634]: ibatis-sqlmap, anteros-core
        s.add("com.ibatis.sqlmap.engine.transaction.jta.JtaTransactionConfig");
        s.add("br.com.anteros.dbcp.AnterosDBCPConfig");

        // [databind#2642]: javax.swing (jdk)
        s.add("javax.swing.JEditorPane");

        // [databind#2648], [databind#2653]: shiro-core
        s.add("org.apache.shiro.realm.jndi.JndiRealmFactory");
        s.add("org.apache.shiro.jndi.JndiObjectFactory");

        // [databind#2658]: ignite-jta (, quartz-core)
        s.add("org.apache.ignite.cache.jta.jndi.CacheJndiTmLookup");
        s.add("org.apache.ignite.cache.jta.jndi.CacheJndiTmFactory");
        s.add("org.quartz.utils.JNDIConnectionProvider");

        // [databind#2659]: aries.transaction.jms
        s.add("org.apache.aries.transaction.jms.internal.XaPooledConnectionFactory");
        s.add("org.apache.aries.transaction.jms.RecoverablePooledConnectionFactory");

        // [databind#2660]: caucho-quercus
        s.add("com.caucho.config.types.ResourceRef");

        // [databind#2662]: aoju/bus-proxy
        s.add("org.aoju.bus.proxy.provider.RmiProvider");
        s.add("org.aoju.bus.proxy.provider.remoting.RmiProvider");

        // [databind#2664]: activemq-core, activemq-pool, activemq-pool-jms

        s.add("org.apache.activemq.ActiveMQConnectionFactory"); // core
        s.add("org.apache.activemq.ActiveMQXAConnectionFactory");
        s.add("org.apache.activemq.spring.ActiveMQConnectionFactory");
        s.add("org.apache.activemq.spring.ActiveMQXAConnectionFactory");
        s.add("org.apache.activemq.pool.JcaPooledConnectionFactory"); // pool
        s.add("org.apache.activemq.pool.PooledConnectionFactory");
        s.add("org.apache.activemq.pool.XaPooledConnectionFactory");
        s.add("org.apache.activemq.jms.pool.XaPooledConnectionFactory"); // pool-jms
        s.add("org.apache.activemq.jms.pool.JcaPooledConnectionFactory");

        // [databind#2666]: apache/commons-jms
        s.add("org.apache.commons.proxy.provider.remoting.RmiProvider");

        // [databind#2682]: commons-jelly
        s.add("org.apache.commons.jelly.impl.Embedded");

        // [databind#2688]: apache/drill
        s.add("oadd.org.apache.xalan.lib.sql.JNDIConnectionPool");

        // [databind#2698]: weblogic w/ oracle/aq-jms
        // (note: dependency not available via Maven Central, but as part of
        // weblogic installation, possibly fairly old version(s))
        s.add("oracle.jms.AQjmsQueueConnectionFactory");
        s.add("oracle.jms.AQjmsXATopicConnectionFactory");
        s.add("oracle.jms.AQjmsTopicConnectionFactory");
        s.add("oracle.jms.AQjmsXAQueueConnectionFactory");
        s.add("oracle.jms.AQjmsXAConnectionFactory");

        // [databind#2764]: org.jsecurity:
        s.add("org.jsecurity.realm.jndi.JndiRealmFactory");

        DEFAULT_NO_DESER_CLASS_NAMES = Collections.unmodifiableSet(s);
    }

    // 上面的拒绝列表来自
    // https://github.com/FasterXML/jackson-databind/blob/master/src/main/java/com/fasterxml/jackson/databind/jsontype/impl/SubTypeValidator.java
    // 下面的逻辑也是, 经过了翻译一次
    public static void check(Class clazz) {
        System.out.println("6666::");
        String className = clazz.getName();
        boolean denied = false;
        if (DEFAULT_NO_DESER_CLASS_NAMES.contains(className)) {
            denied = true;
        }
        if (!denied && className.startsWith(PREFIX_SPRING)) {
            for (Class<?> cls = clazz; (cls != null) && (cls != Object.class); cls = cls.getSuperclass()) {
                String name = cls.getSimpleName();
                // looking for "AbstractBeanFactoryPointcutAdvisor" but no point
                // to allow any is there?
                if ("AbstractPointcutAdvisor".equals(name)
                        // ditto for "FileSystemXmlApplicationContext": block
                        // all ApplicationContexts
                        || "AbstractApplicationContext".equals(name)) {
                    denied = true;
                    break;
                }
            }
        }
        if (!denied && className.startsWith(PREFIX_C3P0)) {
            // [databind#1737]; more 3rd party
            // s.add("com.mchange.v2.c3p0.JndiRefForwardingDataSource");
            // s.add("com.mchange.v2.c3p0.WrapperConnectionPoolDataSource");
            // [databind#1931]; more 3rd party
            // com.mchange.v2.c3p0.ComboPooledDataSource
            // com.mchange.v2.c3p0.debug.AfterCloseLoggingComboPooledDataSource
            if (className.endsWith("DataSource")) {
                denied = true;
            }
        }
        if (denied) {
            throw new UnsupportedOperationException(className + "is in deny-list, so donot supported deserialization using ObjectInputStream");
        }
    }
}

/*
 * 修改历史
 * $Log$ 
 */