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
//作成日: 2013/07/04

package nu.mine.kino.web.line;

import java.util.Map;

import com.linecorp.bot.client.LineBotClient;
import com.linecorp.bot.client.LineBotClientBuilder;
import com.linecorp.bot.client.exception.LineBotAPIException;

import net.arnx.jsonic.JSON;
import nu.mine.kino.web.line.models.Content;
import nu.mine.kino.web.line.models.MessageContent;
import nu.mine.kino.web.line.models.OperationContent;
import nu.mine.kino.web.line.models.LineBotModel;
import nu.mine.kino.web.line.models.Result;

/**
 * @author Masatomi KINO
 * @version $Revision: 530 $
 */
public class LineBotService {

    public Object find(Map<String, String> params) {
        return params;
    }

    public void create(LineBotModel model) throws LineBotAPIException {
        LineBotClient client = createClient();
        Result[] results = model.getResult();

        for (Result result : results) {
            if (isReceivedMessage(result)) {
                MessageContent content = createMessageContent(result);
                String from = content.getFrom();
                String message = from + " さん、" + content.getText() + " って言った？";
                client.sendText(from, message);
            }

            if (isReceivedOperation(result)) {
                OperationContent content = createOperationContent(result);
                String from = content.getParams()[0]; // 追加の際にココの情報を保存しておけば、複数人にBroadcastできそう
                String message = from + " さん登録ありがとうございます！";
                client.sendText(from, message);
            }

        }
    }

    private LineBotClient createClient() {
        return LineBotClientBuilder.create("YOUR_CHANNEL_ID", "YOUR_CHANNEL_SECRET", "YOUR_CHANNEL_MID")
                .build();
    }

    private boolean isReceivedMessage(Result result) {
        return result.getEventType().equals("138311609000106303");
    }

    private boolean isReceivedOperation(Result result) {
        return result.getEventType().equals("138311609100106403");
    }

    private OperationContent createOperationContent(Result result) {
        Map<String, Object> contentMap = result.getContent();
        String encode = JSON.encode(contentMap);
        OperationContent content = JSON.decode(encode, OperationContent.class);
        return content;
    }

    private MessageContent createMessageContent(Result result) {
        Map<String, Object> contentMap = result.getContent();
        String encode = JSON.encode(contentMap);
        MessageContent content = JSON.decode(encode, MessageContent.class);
        return content;
    }

    // public void create2(LineContentModel model) {
    // // log.debug(ExternalContext.getRequest());
    // HttpServletRequest request = ExternalContext.getRequest();
    //
    // // String encode = JSON.encode(text.get("result"));
    // // List<AbstractContent> decode = JSON.decode(encode, List.class);
    //
    // // List<Content> results = (List<Content>) text.get("result");
    // // for (AbstractContent content : decode) {
    // // System.out.println(content);
    // // }
    // System.out.println(model);
    // System.out.println(request);
    // try {
    // StringBuffer buffer = new StringBuffer();
    // BufferedReader reader = new BufferedReader(request.getReader());
    // String line;
    // while ((line = reader.readLine()) != null) {
    // buffer.append(line);
    // buffer.append("\n");
    // }
    //
    // System.out.println("------- body ------");
    // System.out.println(buffer);
    // System.out.println("------- body ------");
    //
    // } catch (IOException e) {
    // // TODO 自動生成された catch ブロック
    // e.printStackTrace();
    // }
    //
    // Enumeration headerNames = request.getHeaderNames();
    // while (headerNames.hasMoreElements()) {
    // String object = (String) headerNames.nextElement();
    // System.out.printf("[%s]: [%s]\n", object,
    // request.getHeader(object));
    // }
    //
    // }
}
