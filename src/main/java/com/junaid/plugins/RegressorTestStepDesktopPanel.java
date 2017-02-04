package com.junaid.plugins;

import com.eviware.soapui.support.UISupport;
import com.eviware.soapui.support.components.JUndoableTextArea;
import com.eviware.soapui.support.components.JUndoableTextField;
import com.eviware.soapui.ui.support.ModelItemDesktopPanel;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.forms.builder.ButtonBarBuilder;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;

/**
 * Created by Junaid on 03-Feb-17.
 */
public class RegressorTestStepDesktopPanel extends ModelItemDesktopPanel<RegressorTestStep>
{
    private JUndoableTextField subjectField;
    private JSplitPane split;
    private JUndoableTextArea messageField;
    private PresentationModel<RegressorTestStep> mailForm;
    public RegressorTestStepDesktopPanel( RegressorTestStep modelItem )
    {
        super(modelItem);
        buildUI();
    }

    private void buildUI()
    {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addFixed( new JLabel( "For Enhancements" ) );
        builder.addRelatedGap();
        subjectField = new JUndoableTextField( "Demo");
//        subjectField.getDocument().addDocumentListener( new DocumentListenerAdapter()
//        {
//            @Override
//            public void update( Document document )
//            {
//                getModelItem().setSubject( subjectField.getText() );
//            }
//        } );

        subjectField.setPreferredSize( new Dimension( 200, 20 ) );
        builder.addFixed(subjectField);

        builder.getPanel().setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        add(builder.getPanel(), BorderLayout.NORTH);
        add( createMessageField(), BorderLayout.CENTER );
        //split = UISupport.createVerticalSplit(createMessageField(), createEmailOptionsField());
        //add( split, BorderLayout.CENTER );

//        SwingUtilities.invokeLater( new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                split.setDividerLocation( 300 );
//            }
//        } );
    }

    private JPanel createMessageField()
    {
        JPanel panel = UISupport.createEmptyPanel( 3, 3, 3, 3 );

        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addFixed( new JLabel( "<html><b>Message content</b></html>" ) );
        panel.add( builder.getPanel(), BorderLayout.NORTH );

        messageField = new JUndoableTextArea( "message" );
//        messageField.getDocument().addDocumentListener( new DocumentListenerAdapter()
//        {
//            @Override
//            public void update( Document document )
//            {
//                getModelItem().setMessage( messageField.getText() );
//            }
//        } );

        panel.add( new JScrollPane( messageField ) );

        return panel;
    }

//    private JComponent createEmailOptionsField()
//    {
//        mailForm = new PresentationModel<RegressorTestStep>( getModelItem() );
//        SimpleBindingForm form = new SimpleBindingForm( mailForm );
//
//        form.appendTextField( "server", "Server", "The SMTP Server to use" );
//        form.appendTextField( "mailTo", "To", "The mail To address" );
//        form.appendTextField( "mailFrom", "From", "The mail From address" );
//
//        form.getPanel().setBorder( BorderFactory.createEmptyBorder( 3, 3, 3, 3 ) );
//
//        return new JScrollPane( form.getPanel() );
//    }

    @Override
    public void propertyChange( PropertyChangeEvent evt )
    {
        super.propertyChange(evt);

        String newValue = String.valueOf( evt.getNewValue() );
        if( evt.getPropertyName().equals( "subject" ) )
        {
            if( !newValue.equals( subjectField.getText() ) )
                subjectField.setText( newValue );
        }
        else if( evt.getPropertyName().equals( "message" ) )
        {
            if( !newValue.equals( messageField.getText() ) )
                messageField.setText( newValue );
        }
    }

}
