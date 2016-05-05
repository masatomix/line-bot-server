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
//�쐬��: 2013/07/04

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
        // OperationContent �̏ꍇ�́A���L�̃p�����^��FROM��ID
        String from = content.getParams()[0]; //
        // �ǉ��̍ۂɃR�R�̏���ۑ����Ă����΁A�����l��Broadcast�ł�����
        String message = from + " ����o�^���肪�Ƃ��������܂��I";
        client.sendText(from, message);
    }

    private void executeContent(LineBotClient client, Result result)
            throws LineBotAPIException {
        MessageContent content = createMessageContent(result);

        // MessageContent �̏ꍇ�́A���L�̃t�B�[���h��FROM��ID
        String from = content.getFrom();
        String messageId = content.getId();

        Map<String, Object> contentMetaData = content.getContentMetadata();

        String message = null;

        ContentType contentType = content.getContentType();
        switch (contentType) {
        case TEXT:
            message = from + " ����A\n" + content.getText() + " ���Č������H";
            break;
        case IMAGE:
        case VIDEO:
        case AUDIO:
            CloseableMessageContent binaryContent = client
                    .getMessageContent(messageId);
            message = binaryContent.getFileName() + " ���������Ă��ꂽ�H";
            break;
        case LOCATION:
            double lat = content.getLocation().getLatitude();
            double longT = content.getLocation().getLongitude();
            message = String.format("%s ����A\n" + "�ܓx: %s, �o�x: %s" + " �ɂ��܂��H",
                    from, lat, longT);
            break;
        case STICKER:
            message = contentMetaData.toString();

            break;
        case CONTACT:
            String displayName = (String) contentMetaData.get("displayName");
            message = displayName + " ����̏����������Ă��ꂽ�H";
            String mid = (String) contentMetaData.get("mid");
            UserProfileResponse userProfile = client
                    .getUserProfile(Arrays.asList(new String[] { mid }));

            String encode = JSON.encode(userProfile,true);
            log.debug(encode);
            
            break;
        default:
            message = from + " ����A\n����ɂ��́B";
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
    // // TODO �����������ꂽ catch �u���b�N
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
     * �g138311609000106303�h    Received message (example: text, images)
     * �g138311609100106403�h    Received operation (example: added as friend)
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
     * �g138311609000106303�h    Received message (example: text, images)
     * �g138311609100106403�h    Received operation (example: added as friend)
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
