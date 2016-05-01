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
//作成日: 2016/02/21

package nu.mine.kino.web.exceptions;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class STATUS_Exception extends Exception {

    /**
     * <code>serialVersionUID</code> のコメント
     */
    private static final long serialVersionUID = -7926913582552724160L;
    private Object obj;

    public STATUS_Exception() {
        super();
        // TODO 自動生成されたコンストラクター・スタブ
    }

    public STATUS_Exception(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO 自動生成されたコンストラクター・スタブ
    }

    public STATUS_Exception(String message, Throwable cause) {
        super(message, cause);
        // TODO 自動生成されたコンストラクター・スタブ
    }

    public STATUS_Exception(String message) {
        super(message);
        // TODO 自動生成されたコンストラクター・スタブ
    }

    public STATUS_Exception(Throwable cause) {
        super(cause);
        // TODO 自動生成されたコンストラクター・スタブ
    }

    public STATUS_Exception setInformation(Object obj) {
        this.obj = obj;
        return this;
    }

    public Object getInformation() {
        return obj;
    }

}
