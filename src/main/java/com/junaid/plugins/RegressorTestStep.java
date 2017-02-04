package com.junaid.plugins;

import com.eviware.soapui.config.TestStepConfig;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestRequestStepResult;
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
        //SoapUI.log("bu!");

        TestStepResult result = compareResponses(testRunner);
        return result;
    }

    private TestStepResult compareResponses(TestCaseRunner testRunner) {
        TestStepResult.TestStepStatus testStepStatus = TestStepResult.TestStepStatus.FAILED;
        StringBuilder buffer =new StringBuilder();
        List<String> responses = new ArrayList<>();

        RegressorTestStepResult result = new RegressorTestStepResult(this);
        List<TestStepResult> testRunnerResults = testRunner.getResults();
        for(TestStepResult testStepResult :testRunnerResults){
            if(testStepResult instanceof WsdlTestRequestStepResult){
                buffer.append(testStepResult.getTestStep().getName());
                responses.add(((WsdlTestRequestStepResult) testStepResult).getResponseContent());
                result.addtestRequestStepResult((WsdlTestRequestStepResult) testStepResult);
            }
        }
        int responsesSize = responses.size();
        switch (responsesSize){
            case 0:{
                result.addMessage("No response found!");
                break;
            }
            case 1:{
                result.addMessage("Single response found, cannot compare!");
                break;
            }
            default:{
                boolean areAllResponsesEqual=true;
                String controlResponse = responses.get(0)==null?"":responses.get(0);
                for(String candidateResponse : responses){
                    if(!controlResponse.equals(candidateResponse)){
                        areAllResponsesEqual=false;
                        break;
                    }
                }
                if(areAllResponsesEqual){
                    result.addMessage("Responses match!");
                    testStepStatus= TestStepResult.TestStepStatus.OK;
                }
                else{
                    result.addMessage("Responses do not match!");
                }
//                if(controlResponse==null){
//                    result.addMessage("Could not compare since response 1 not found");
//                }
//                else {
//                    for(String candidateResponse : responses){
//                        controlResponse.equals(candidateResponse);
//                    }
//                }
            }
        }

        result.addMessage("Grabbed Responses : " + responses);
        result.setStatus(testStepStatus);
        return result;
    }
}


