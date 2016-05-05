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

import static nu.mine.kino.web.line.Constants.YOUR_CHANNEL_ID;
import static nu.mine.kino.web.line.Constants.YOUR_CHANNEL_MID;
import static nu.mine.kino.web.line.Constants.YOUR_CHANNEL_SECRET;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.linecorp.bot.client.CloseableMessageContent;
import com.linecorp.bot.client.LineBotClient;
import com.linecorp.bot.client.LineBotClientBuilder;
import com.linecorp.bot.client.exception.LineBotAPIException;
import com.linecorp.bot.model.profile.UserProfileResponse;

import lombok.extern.slf4j.Slf4j;
import net.arnx.jsonic.JSON;
import nu.mine.kino.web.line.models.ContentType;
import nu.mine.kino.web.line.models.LineBotModel;
import nu.mine.kino.web.line.models.MessageContent;
import nu.mine.kino.web.line.models.OperationContent;
import nu.mine.kino.web.line.models.Result;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
@Service
@Slf4j
public class LineBotService {
    // public Object find(Map<String, String> params) {
    // return params;
    // }

    // public void create(Map<String, Object> model) throws LineBotAPIException
    // {
    // log.debug("-------------");
    // log.debug(JSON.encode(model, true));
    // log.debug("-------------");
    // }

    public void create(LineBotModel model) throws LineBotAPIException {
        LineBotClient client = createClient();
        Result[] results = model.getResult();

        for (Result result : results) {
            if (isReceivedMessage(result)) {
                executeContent(client, result);
            }

            if (isReceivedOperation(result)) {
                executeOperation(client, result);
            }

        }
    }

    private void executeOperation(LineBotClient client, Result result)
            throws LineBotAPIException {
        OperationContent content = createOperationContent(result);
        // OperationContent の場合は、下記のパラメタがFROMのID
        String from = content.getParams()[0]; //
        // 追加の際にココの情報を保存しておけば、複数人にBroadcastできそう
        String message = from + " さん登録ありがとうございます！";
        client.sendText(from, message);
    }

    private void executeContent(LineBotClient client, Result result)
            throws LineBotAPIException {
        MessageContent content = createMessageContent(result);

        // MessageContent の場合は、下記のフィールドがFROMのID
        String from = content.getFrom();
        String messageId = content.getId();

        Map<String, Object> contentMetaData = content.getContentMetadata();

        String message = null;

        ContentType contentType = content.getContentType();
        switch (contentType) {
        case TEXT:
            message = from + " さん、\n" + content.getText() + " って言った？";
            break;
        case IMAGE:
        case VIDEO:
        case AUDIO:
            CloseableMessageContent binaryContent = client
                    .getMessageContent(messageId);
            message = binaryContent.getFileName() + " をおくってくれた？";
            break;
        case LOCATION:
            double lat = content.getLocation().getLatitude();
            double longT = content.getLocation().getLongitude();
            message = String.format("%s さん、\n" + "緯度: %s, 経度: %s" + " にいます？",
                    from, lat, longT);
            break;
        case STICKER:
            message = contentMetaData.toString();

            break;
        case CONTACT:
            String displayName = (String) contentMetaData.get("displayName");
            message = displayName + " さんの情報をおくってくれた？";
            String mid = (String) contentMetaData.get("mid");
            UserProfileResponse userProfile = client
                    .getUserProfile(Arrays.asList(new String[] { mid }));

            String encode = JSON.encode(userProfile,true);
            log.debug(encode);
            
            break;
        default:
            message = from + " さん、\nこんにちは。";
            break;
        }
        client.sendText(from, message);
    }

    private LineBotClient createClient() {
        return LineBotClientBuilder
                .create(YOUR_CHANNEL_ID, YOUR_CHANNEL_SECRET, YOUR_CHANNEL_MID)
                .build();
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

    /**
     * <pre>
     * Values of the eventType property:
     * Value   Description
     * “138311609000106303”    Received message (example: text, images)
     * “138311609100106403”    Received operation (example: added as friend)
     * 
     * https://developers.line.me/bot-api/getting-started-with-bot-api-trial
     * </pre>
     * 
     * @param result
     * @return
     */
    private boolean isReceivedMessage(Result result) {
        return result.getEventType().equals("138311609000106303");
    }

    /**
     * <pre>
     * Values of the eventType property:
     * Value   Description
     * “138311609000106303”    Received message (example: text, images)
     * “138311609100106403”    Received operation (example: added as friend)
     * 
     * https://developers.line.me/bot-api/getting-started-with-bot-api-trial
     * </pre>
     * 
     * @param result
     * @return
     */
    private boolean isReceivedOperation(Result result) {
        return result.getEventType().equals("138311609100106403");
    }
}
