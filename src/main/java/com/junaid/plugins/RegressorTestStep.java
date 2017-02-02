package com.junaid.plugins;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.config.TestStepConfig;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestRequestStepResult;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestStepResult;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestStepWithProperties;
import com.eviware.soapui.model.testsuite.TestCaseRunContext;
import com.eviware.soapui.model.testsuite.TestCaseRunner;
import com.eviware.soapui.model.testsuite.TestStepResult;
import com.eviware.soapui.plugins.auto.PluginTestStep;
import com.eviware.soapui.support.UISupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junaid on 03-Feb-17.
 */


@PluginTestStep(typeName = "RegressorTestStep", name = "Regressor TestStep", description = "Compares all requests responses")
public class RegressorTestStep extends WsdlTestStepWithProperties {

    public RegressorTestStep(WsdlTestCase testCase, TestStepConfig config, boolean forLoadTest) {
        super(testCase, config, true, forLoadTest);
        if( !forLoadTest )
        {
            setIcon( UISupport.createImageIcon("email.png") );
        }

    }

    @Override
    public TestStepResult run(TestCaseRunner testRunner, TestCaseRunContext testRunContext) {
        SoapUI.log("bu!");
        StringBuffer buffer =new StringBuffer();
        List<String> responses = new ArrayList<>();
        WsdlTestStepResult result = new WsdlTestStepResult(this);
        List<TestStepResult> results = testRunner.getResults();
        for(TestStepResult testStepResult :results){
            if(testStepResult instanceof WsdlTestRequestStepResult){
                buffer.append(testStepResult.getTestStep().getName());
                responses.add(((WsdlTestRequestStepResult) testStepResult).getResponseContentAsXml());
            }
        }
        result.addMessage("bu! "+responses);
        result.setStatus( TestStepResult.TestStepStatus.OK );
        return result;
    }
}


