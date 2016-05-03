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
//作成日: 2016/05/03

package nu.mine.kino.web.line.models;

import java.util.Date;
import java.util.Map;

import lombok.Data;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
@Data
public class Result {
    // private GeneralContent content;

    // contentについては、リクエストによって仕様が異なるためMapで持たせざるを得ない
    private Map<String, Object> content;

    private Date createdTime;

    private String eventType;

    private String from;

    private String fromChannel;

    private String id;

    private String[] to;

    private int toType;

    private int toChannel;
}
