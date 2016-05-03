/******************************************************************************
 * Copyright (c) 2010 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//çÏê¨ì˙: 2016/05/03

package nu.mine.kino.web.line.models;

import java.util.Date;

import lombok.Data;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
@Data
public class MessageContent implements Content {

    private ContentMetadata contentMetadata;

    private String contentType;

    private Date createdTime;

    private Date deliveredTime;

    private String from;

    private String id;

    private Location location;

    private String seq;

    private String text;

    private String[] to;

    private int toType;

}
