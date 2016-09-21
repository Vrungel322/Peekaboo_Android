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
        Message message = new Message(Message.Command.MESSAGE);
        message.addParam(Message.Params.TYPE, Message.Type.TEXT);
        message.addParam(Message.Params.DATE, "asd1");
        message.addParam(Message.Params.REASON, "asd2");

        Message transform = btm.transform(mtb.transform(message));
        assertEquals(message, transform);
    }

    @Test
    public void toStringTest() {
        Message withBodyText = new Message(Message.Command.MESSAGE)
                .addParam(Message.Params.TYPE, Message.Type.TEXT)
                .setTextBody("asd");

        Message withoutBodyText = new Message(Message.Command.MESSAGE)
                .addParam(Message.Params.TYPE, Message.Type.TEXT);

        Message withBodyBinary = new Message(Message.Command.MESSAGE)
                .addParam(Message.Params.TYPE, Message.Type.AUDIO)
                .setBody(new byte[] {1,2,3});

        Message withoutBodyBinary = new Message(Message.Command.MESSAGE)
                .addParam(Message.Params.TYPE, Message.Type.AUDIO);

        btm.transform(mtb.transform(withBodyText)).toString();
        btm.transform(mtb.transform(withoutBodyText)).toString();
        btm.transform(mtb.transform(withBodyBinary)).toString();
        btm.transform(mtb.transform(withoutBodyBinary)).toString();
    }


    @Test
    public void checkTextBody() {
        Message message = new Message(Message.Command.MESSAGE);
        message.addParam(Message.Params.TYPE, Message.Type.TEXT);
        message.addParam(Message.Params.DATE, "asd1");
        message.addParam(Message.Params.REASON, "asd2");

        Message transform = btm.transform(mtb.transform(message));
        assertEquals(message, transform);

        message.setTextBody("asdasd");
        transform = btm.transform(mtb.transform(message));
        assertEquals(message, transform);

    }

    @Test
    public void checkBinaryBody() {
        Message message = new Message(Message.Command.MESSAGE);
        message.addParam(Message.Params.TYPE, Message.Type.AUDIO);
        message.addParam(Message.Params.DATE, "asd1");
        message.addParam(Message.Params.REASON, "asd2");

        Message transform = btm.transform(mtb.transform(message));
        assertEquals(message, transform);

        message.setBody(new byte[]{1, 2, 3});
        transform = btm.transform(mtb.transform(message));
        assertEquals(message, transform);
    }
}
