package nu.mine.kino.web.line.models;

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
//çÏê¨ì˙: 2016/05/04

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public enum ContentType {

    NONE_0,

    TEXT,

    IMAGE,

    VIDEO,

    AUDIO,

    NONE_5,

    NONE_6,

    LOCATION,

    STICKER,

    NONE_9,

    CONTACT,

    // NONE_11,
    //
    // RICH_MESSAGE,

    ;

    public int getTypeCode() {
        return this.ordinal();
    }

}
