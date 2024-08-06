/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.csaude.pds;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.CompositeTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.openmrs.api.APIException;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.csaude.pds.listener.config.utils.PdsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static org.openmrs.util.OpenmrsUtil.getApplicationDataDirectory;

public class PdsActivator extends BaseModuleActivator {
	
	private static final Logger log = LoggerFactory.getLogger(PdsActivator.class);
	
	protected static final String PDS_APPENDER_NAME = "PDS_APPENDER";
	
	protected static final String DIR_PDS = "pds";
	
	protected static final String DIR_LOGS = "logs";
	
	protected static final String LOG_FILE = "pds.log";
	
	protected static final String LAYOUT = "%-5p %t - %C{1}.%M(%L) |%d{ISO8601}| %m%n";
	
	protected static final String LOG_FILE_PATTERN = LOG_FILE + ".%d{yyyy-MM-dd}-%i";
	
	/**
	 * @see BaseModuleActivator#started()
	 */
	@Override
	public void started() {
		log.info("PDS module started");
		log.info("Adding PDS log file to log4j configuration");
		
		try {
			File pdsLogFile = PdsUtils.createPath(getApplicationDataDirectory(), DIR_PDS, DIR_LOGS, LOG_FILE).toFile();
			String logFileName = pdsLogFile.getAbsolutePath();
			String logFilePattern = PdsUtils.createPath(pdsLogFile.getParent(), LOG_FILE_PATTERN).toFile().getAbsolutePath();
			LoggerContext context = (LoggerContext) LogManager.getContext(false);
			Configuration cfg = context.getConfiguration();
			PatternLayout layout = PatternLayout.newBuilder().withPattern(LAYOUT).build();
			TriggeringPolicy timePolicy = TimeBasedTriggeringPolicy.newBuilder().build();
			//TODO Make max file size configurable
			TriggeringPolicy sizePolicy = SizeBasedTriggeringPolicy.createPolicy("50 MB");
			TriggeringPolicy policy = CompositeTriggeringPolicy.createPolicy(timePolicy, sizePolicy);
			RollingFileAppender pdsAppender = RollingFileAppender.newBuilder().setConfiguration(cfg)
			        .setName(PDS_APPENDER_NAME).withFileName(logFileName).setLayout(layout).withAppend(true)
			        .withFilePattern(logFilePattern).withPolicy(policy).build();
			AppenderRef appenderRef = AppenderRef.createAppenderRef(PDS_APPENDER_NAME, null, null);
			LoggerConfig loggerCfg = LoggerConfig.newBuilder().withConfig(cfg).withLoggerName(getPdsLoggerName())
			        .withAdditivity(false).withLevel(Level.INFO).withRefs(new AppenderRef[] { appenderRef }).build();
			loggerCfg.addAppender(pdsAppender, null, null);
			cfg.addLogger(getPdsLoggerName(), loggerCfg);
			pdsAppender.start();
			cfg.addAppender(pdsAppender);
			context.updateLoggers();
		}
		catch (Exception e) {
			throw new APIException(e);
		}
		
	}
	
	/**
	 * @see BaseModuleActivator#stopped()
	 */
	@Override
	public void stopped() {
		log.info("PDS module stopped");
		log.info("Removing PDS log file from log4j configuration");
		
		LoggerContext context = (LoggerContext) LogManager.getContext(false);
		Configuration cfg = context.getConfiguration();
		cfg.removeLogger(getPdsLoggerName());
		cfg.getAppender(PDS_APPENDER_NAME).stop();
		cfg.getAppenders().remove(PDS_APPENDER_NAME);
		context.updateLoggers();
		
	}
	
	protected String getPdsLoggerName() {
		return getClass().getPackage().getName();
	}
	
}
