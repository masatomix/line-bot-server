/******************************************************************************
 * Copyright (c) 2014 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//çÏê¨ì˙: 2016/02/03

package nu.mine.kino.web;

import java.lang.reflect.Method;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.support.WebApplicationContextUtils;

import net.arnx.jsonic.web.Container;
import net.arnx.jsonic.web.ExternalContext;

public class JSONICContainer extends Container {
    Log log;

    ApplicationContext appContext;

    @Override
    public void init(HttpServlet servlet) throws ServletException {
        super.init(servlet);
        this.log = LogFactory.getLog(servlet.getClass());
        appContext = WebApplicationContextUtils
                .getWebApplicationContext(context);

    }

    @Override
    public Object getComponent(String className) throws Exception {
        printheeader();
        Object component;
        try {
            component = appContext.getBean(className);
        } catch (Exception e) {
            throw new ClassNotFoundException("class not found: " + className,
                    e);
        }

        if (component instanceof ApplicationContextAware) {
            ((ApplicationContextAware) component)
                    .setApplicationContext(appContext);
        }

        for (Method method : component.getClass().getMethods()) {
            Class<?>[] params = method.getParameterTypes();
            if (void.class.equals(method.getReturnType())
                    && method.getName().startsWith("set")
                    && params.length == 1) {
                Class<?> c = params[0];
                if (HttpServletRequest.class.equals(c)) {
                    method.invoke(component, ExternalContext.getRequest());
                } else if (HttpServletResponse.class.equals(c)) {
                    method.invoke(component, ExternalContext.getResponse());
                }
            }
        }

        return component;
    }

    private void printheeader() {
        Enumeration<String> headerNames = ExternalContext.getRequest()
                .getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String string = (String) headerNames.nextElement();
            String header = ExternalContext.getRequest().getHeader(string);
            log.debug(string + ":::::::" + header);
        }
    }

    @Override
    public boolean isDebugMode() {
        return (debug != null) ? debug : log.isDebugEnabled();
    }

    @Override
    public void debug(String message, Throwable e) {
        if (e != null) {
            log.debug(message, e);
        } else {
            log.debug(message);
        }
    }

    @Override
    public void warn(String message, Throwable e) {
        if (e != null) {
            log.warn(message, e);
        } else {
            log.warn(message);
        }
    }

    @Override
    public void error(String message, Throwable e) {
        if (e != null) {
            log.error(message, e);
        } else {
            log.error(message);
        }
    }
}
