package com.junaid.plugins;

import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestRequestStepResult;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestStep;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestStepResult;
import com.eviware.soapui.support.action.swing.ActionList;
import com.junaid.plugins.actions.ShowRegressorMessageExchangeAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junaid on 04-Feb-17.
 */
public class RegressorTestStepResult extends WsdlTestStepResult {
    private boolean addedAction;
    List<WsdlTestRequestStepResult> testRequestStepResults;
    public RegressorTestStepResult(WsdlTestStep testStep) {
        super(testStep);
        this.testRequestStepResults=new ArrayList<>();
    }

    public ActionList getActions() {
        if(!this.addedAction) {
            this.addAction(new ShowRegressorMessageExchangeAction(this.testRequestStepResults, "TestStep"), true);
            this.addedAction = true;
        }

        return super.getActions();
    }

    public void addtestRequestStepResult(WsdlTestRequestStepResult testRequestStepResult){
        this.testRequestStepResults.add(testRequestStepResult);
    }
}
