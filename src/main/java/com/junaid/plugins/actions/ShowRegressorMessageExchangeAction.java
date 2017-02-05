package com.junaid.plugins.actions;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.impl.wsdl.panels.request.StringToStringMapTableModel;
import com.eviware.soapui.impl.wsdl.support.MessageExchangeRequestMessageEditor;
import com.eviware.soapui.impl.wsdl.support.MessageExchangeResponseMessageEditor;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestRequestStepResult;
import com.eviware.soapui.model.ModelItem;
import com.eviware.soapui.model.iface.MessageExchange;
import com.eviware.soapui.model.support.ModelSupport;
import com.eviware.soapui.model.testsuite.AssertedXPath;
import com.eviware.soapui.model.testsuite.RequestAssertedMessageExchange;
import com.eviware.soapui.model.testsuite.ResponseAssertedMessageExchange;
import com.eviware.soapui.support.DescriptionPanel;
import com.eviware.soapui.support.UISupport;
import com.eviware.soapui.support.swing.JTableFactory;
import com.eviware.soapui.support.types.StringToStringMap;
import com.eviware.soapui.ui.desktop.DesktopPanel;
import com.eviware.soapui.ui.support.DefaultDesktopPanel;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Junaid on 04-Feb-17.
 */
public class ShowRegressorMessageExchangeAction extends AbstractAction {
    private DefaultDesktopPanel desktopPanel;
    private final List<MessageExchange> messageExchanges;
    private final String ownerName;
    private MessageExchangeResponseMessageEditor responseMessageEditor;
    private MessageExchangeRequestMessageEditor requestMessageEditor;

    public ShowRegressorMessageExchangeAction(List<WsdlTestRequestStepResult> wsdlTestRequestStepResults, String ownerName) {
        super("Show Message Exchange");
        this.ownerName = ownerName;
        this.messageExchanges=new ArrayList<>();
        for(WsdlTestRequestStepResult wsdlTestRequestStepResult:wsdlTestRequestStepResults){
            messageExchanges.add(wsdlTestRequestStepResult.getMessageExchanges()[0]);
        }
    }

    public void actionPerformed(ActionEvent e) {
        try {
            UISupport.showDesktopPanel(this.buildFrame());
        } catch (Exception var3) {
            SoapUI.logError(var3);
        }

    }

    private DesktopPanel buildFrame() {
        if(this.desktopPanel == null) {
            this.desktopPanel = new ShowRegressorMessageExchangeAction.MessageExchangeDesktopPanel("Regression Results Viewer", "Message for " + this.ownerName, this.buildContent());
        }

        return this.desktopPanel;
    }

    private JComponent buildContent() {
        JTabbedPane messageTabs = new JTabbedPane();
        for(MessageExchange messageExchange: this.messageExchanges){
            buildTabsForMessageExchange(messageTabs, messageExchange);
        }
        messageTabs.setPreferredSize(new Dimension(500, 400));
        JPanel tabPanel = UISupport.createTabPanel(messageTabs, true);
        DescriptionPanel descriptionPanel = UISupport.buildDescription("Regression Results", "See All the requests/responses messages below", (ImageIcon)null);
        tabPanel.add(descriptionPanel, "North");
        return tabPanel;
    }

    private void buildTabsForMessageExchange(JTabbedPane messageTabs, MessageExchange messageExchange) {
        String requestName = "["+messageExchange.getModelItem().getName()+"]-";
        messageTabs.addTab(requestName +"Request Message", this.buildRequestTab(messageExchange));
        messageTabs.addTab(requestName +"Response Message", this.buildResponseTab(messageExchange));
        messageTabs.addTab(requestName +"Properties", this.buildPropertiesTab(messageExchange));
        String[] messages = messageExchange.getMessages();
        if(messages != null && messages.length > 0) {
            messageTabs.addTab(requestName+"Messages", this.buildMessagesTab(messageExchange));
        }

        if(this.getAssertedXPaths(messageExchange).size() > 0) {
            messageTabs.addTab(requestName+"XPath Assertions", this.buildAssertionsTab(messageExchange));
        }
    }

    private Component buildAssertionsTab(MessageExchange messageExchange) {
        java.util.List assertedXPaths = this.getAssertedXPaths(messageExchange);
        DefaultTableModel tm = new DefaultTableModel(assertedXPaths.size(), 2);
        tm.setColumnIdentifiers(new String[]{"Label", "XPath"});
        JXTable table = JTableFactory.getInstance().makeJXTable(tm);
        table.setHorizontalScrollEnabled(true);
        table.getColumn(0).setPreferredWidth(100);

        for(int c = 0; c < assertedXPaths.size(); ++c) {
            tm.setValueAt(((AssertedXPath)assertedXPaths.get(c)).getLabel(), c, 0);
            tm.setValueAt(((AssertedXPath)assertedXPaths.get(c)).getPath(), c, 1);
        }

        return new JScrollPane(table);
    }

    private java.util.List<AssertedXPath> getAssertedXPaths(MessageExchange messageExchange) {
        ArrayList assertedXPaths = new ArrayList();
        AssertedXPath[] xpaths;
        if(messageExchange instanceof RequestAssertedMessageExchange) {
            xpaths = ((RequestAssertedMessageExchange)messageExchange).getAssertedXPathsForRequest();
            if(xpaths != null && xpaths.length > 0) {
                assertedXPaths.addAll(Arrays.asList(xpaths));
            }
        }

        if(messageExchange instanceof ResponseAssertedMessageExchange) {
            xpaths = ((ResponseAssertedMessageExchange)messageExchange).getAssertedXPathsForResponse();
            if(xpaths != null && xpaths.length > 0) {
                assertedXPaths.addAll(Arrays.asList(xpaths));
            }
        }

        return assertedXPaths;
    }

    private Component buildPropertiesTab(MessageExchange messageExchange) {
        StringToStringMap properties = new StringToStringMap();
        if(messageExchange != null && messageExchange.getProperties() != null) {
            properties.putAll(messageExchange.getProperties());
            properties.put("Timestamp", (new Date(messageExchange.getTimestamp())).toString());
            properties.put("Time Taken", String.valueOf(messageExchange.getTimeTaken()));
        }

        JTable table = JTableFactory.getInstance().makeJTable(new StringToStringMapTableModel(properties, "Name", "Value", false));
        return new JScrollPane(table);
    }

    private Component buildMessagesTab(MessageExchange messageExchange) {
        String[] messages = messageExchange.getMessages();
        return (Component)(messages != null && messages.length != 0?new JScrollPane(new JList(messages)):new JLabel("No messages to display"));
    }

    private Component buildResponseTab(MessageExchange messageExchange) {
        this.responseMessageEditor = new MessageExchangeResponseMessageEditor(messageExchange);
        return this.responseMessageEditor;
    }

    private Component buildRequestTab(MessageExchange messageExchange) {
        this.requestMessageEditor = new MessageExchangeRequestMessageEditor(messageExchange);
        return this.requestMessageEditor;
    }

    private final class MessageExchangeDesktopPanel extends DefaultDesktopPanel {
        private MessageExchangeDesktopPanel(String title, String description, JComponent component) {
            super(title, description, component);
        }

        public boolean onClose(boolean canCancel) {
            MessageExchangeRequestMessageEditor requestMessageEditor = ShowRegressorMessageExchangeAction.this.requestMessageEditor;
            if (requestMessageEditor!=null) {
                requestMessageEditor.release();
            }
            MessageExchangeResponseMessageEditor responseMessageEditor = ShowRegressorMessageExchangeAction.this.responseMessageEditor;
            if (requestMessageEditor!=null) {
                responseMessageEditor.release();
            }
            ShowRegressorMessageExchangeAction.this.desktopPanel = null;
            return super.onClose(canCancel);
        }

        public boolean dependsOn(ModelItem modelItem) {
            return ModelSupport.dependsOn(ShowRegressorMessageExchangeAction.this.messageExchanges.get(0).getModelItem(), modelItem);
        }
    }

}
