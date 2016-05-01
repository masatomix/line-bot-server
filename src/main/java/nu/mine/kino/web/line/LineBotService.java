/******************************************************************************
 * Copyright (c) 2012 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id: AjaxTextController.java 530 2013-09-16 11:15:36Z masatomix $
 ******************************************************************************/
//çÏê¨ì˙: 2013/07/04

package nu.mine.kino.web.line;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.arnx.jsonic.web.ExternalContext;

/**
 * @author Masatomi KINO
 * @version $Revision: 530 $
 */
public class LineBotService {

    public Object find(Map<String, String> params) {
        return params;
    }

    public void create(Map<String, Object> text) {
        // log.debug(ExternalContext.getRequest());
        HttpServletRequest request = ExternalContext.getRequest();
        System.out.println(text);
        System.out.println(request);
        try {
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = new BufferedReader(request.getReader());
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
                buffer.append("\n");
            }

            System.out.println("------- body ------");
            System.out.println(buffer);
            System.out.println("------- body ------");

        } catch (IOException e) {
            // TODO é©ìÆê∂ê¨Ç≥ÇÍÇΩ catch ÉuÉçÉbÉN
            e.printStackTrace();
        }

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String object = (String) headerNames.nextElement();
            System.out.printf("[%s]: [%s]\n", object,
                    request.getHeader(object));
        }

    }
}
