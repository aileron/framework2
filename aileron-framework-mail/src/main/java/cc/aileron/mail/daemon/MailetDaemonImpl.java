/*
 * Copyright (C) 2009 aileron.cc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.aileron.mail.daemon;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.aileron.mail.MailetApplication;
import cc.aileron.mail.MailetDaemon;
import cc.aileron.mail.MailetSetting;
import cc.aileron.mail.WrongMessageException;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aileron
 */
@Singleton
public class MailetDaemonImpl implements MailetDaemon
{
    @Override
    public Void call() throws Exception
    {
        folder.open(Folder.READ_WRITE);
        while (isActive)
        {
            final int unredCount = folder.getUnreadMessageCount();
            if (unredCount > 0)
            {
                for (final Message message : folder.getMessages())
                {
                    try
                    {
                        application.execute(message);
                    }
                    catch (final WrongMessageException e)
                    {
                        final String from = message.getFrom()[0].toString();
                        final String to = message.getRecipients(RecipientType.TO)[0].toString();
                        final String subject = message.getSubject();
                        final String content = message.getContent().toString();
                        logger.error(String.format("mail(from:%s,to:%s,subject:%s)=%s",
                                from,
                                to,
                                subject,
                                content));
                    }
                    catch (final Throwable th)
                    {
                        final String from = message.getFrom()[0].toString();
                        final String to = message.getRecipients(RecipientType.TO)[0].toString();
                        final String subject = message.getSubject();
                        final String content = message.getContent().toString();
                        logger.error(String.format("mail(from:%s,to:%s,subject:%s)=%s",
                                from,
                                to,
                                subject,
                                content),
                                th);
                        isActive = false;
                    }
                    finally
                    {
                        message.setFlag(Flags.Flag.SEEN, true);
                    }
                }
                // folder.close(false);
                // folder.open(Folder.READ_WRITE);
            }
            Thread.sleep(100);
        }
        if (folder.isOpen())
        {
            folder.close(false);
        }
        return null;
    }

    /**
     * @param setting
     * @param application
     * @throws MessagingException
     */
    @Inject
    public MailetDaemonImpl(final MailetSetting setting,
            final MailetApplication application) throws MessagingException
    {
        this.application = application;
        final Session session = Session.getDefaultInstance(setting.properties(),
                null);
        final Store store = session.getStore(setting.type()
                .name()
                .toLowerCase());
        store.connect(setting.host(),
                setting.port(),
                setting.account(),
                setting.password());
        this.folder = store.getFolder(setting.folderName());
    }

    /**
     * logger
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MailetApplication application;
    private final Folder folder;
    private volatile boolean isActive = true;
}
