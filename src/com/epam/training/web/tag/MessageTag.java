package com.epam.training.web.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

public class MessageTag extends BodyTagSupport {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3971512824894449337L;
	private static final String ATTR_LOCALE = "locale";
    private static final String RU = "ru_RU";

    private String messageKey;

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public int doStartTag() throws JspException {
        if (!messageKey.isEmpty()) {
            MessageManager messageManager;
            if (RU.equals(pageContext.getSession().getAttribute(ATTR_LOCALE))) {
                messageManager = MessageManager.RU;
            } else {
                messageManager = MessageManager.EN;
            }

            try {
                pageContext.getOut().print(messageManager.getMessage(messageKey));
            } catch (IOException e) {
                throw new JspException(e);
            }
        }

        return SKIP_BODY;
    }
}