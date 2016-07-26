package com.peekaboo.data.mapper;

import com.peekaboo.data.mappers.ByteArrayToMessageMapper;
import com.peekaboo.data.mappers.MessageToByteArrayMapper;
import com.peekaboo.presentation.services.Message;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class MessageMappersTest {

    private MessageToByteArrayMapper mtb = new MessageToByteArrayMapper();
    private ByteArrayToMessageMapper btm = new ByteArrayToMessageMapper();

    @Test
    public void checkBodylessConvertation() {


        Message message = new Message(Message.Command.SEND);
        message.addParam(Message.Params.TYPE, "audio");
        message.addParam(Message.Params.DATE, "asd1");
        message.addParam(Message.Params.REASON, "asd2");


//        message.addParam(Message.Params.TYPE, "text");
//        message.setBody(new byte[] {1,2,3});
        assertEquals(message, btm.transform(mtb.transform(message)));
    }

    @Test
    public void checkTextBody() {
        Message message = new Message(Message.Command.SEND);
        message.addParam(Message.Params.TYPE, "text");
        message.addParam(Message.Params.DATE, "asd1");
        message.addParam(Message.Params.REASON, "asd2");

        message.setTextBody("asdasd");

        assertEquals(message, btm.transform(mtb.transform(message)));
    }

    @Test
    public void checkBinaryBody() {
        Message message = new Message(Message.Command.SEND);
        message.addParam(Message.Params.TYPE, "text");
        message.addParam(Message.Params.DATE, "asd1");
        message.addParam(Message.Params.REASON, "asd2");

        message.setBody(new byte[] {1,2,3});

        assertEquals(message, btm.transform(mtb.transform(message)));
    }
}
