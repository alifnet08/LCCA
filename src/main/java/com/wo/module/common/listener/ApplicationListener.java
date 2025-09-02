package com.wo.module.common.listener;

import javax.faces.application.Application;
import javax.faces.event.AbortProcessingException;
//import javax.faces.event.PostConstructApplicationEvent;
//import javax.faces.event.PreDestroyApplicationEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

//import org.apache.log4j.Logger;

//import com.wo.erec.common.util.ODFConverterUtil;

public class ApplicationListener implements SystemEventListener {

	// private static Logger logger =
	// Logger.getLogger(ApplicationListener.class);

	public boolean isListenerForSource(Object app) {
		boolean result = false;
		result = (app instanceof Application);
		return result;
	}

	public void processEvent(SystemEvent event) throws AbortProcessingException {
		// if (event instanceof PostConstructApplicationEvent) {
		// logger.debug("Post Application Event Listener process [start]....");
		// try {
		// ODFConverterUtil.start();
		// } catch (Throwable ex) {
		// logger.error(
		// "Error starting open office task process, error message : "
		// + ex.getMessage(), ex);
		// }
		// logger.debug("Post Application Event Listener process [end]....");
		//
		// } else if (event instanceof PreDestroyApplicationEvent) {
		// logger.debug("Pre Destroy Application Event Listener process [start]....");
		// try {
		// ODFConverterUtil.stop();
		// } catch (Throwable ex) {
		// logger.error(
		// "Error stopping open office task process, error message : "
		// + ex.getMessage(), ex);
		// }
		//
		// logger.debug("Pre Destroy Application Event Listener process [end]....");
		// }
	}

}
