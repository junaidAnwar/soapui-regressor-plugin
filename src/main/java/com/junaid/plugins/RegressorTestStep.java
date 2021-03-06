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
import java.util.Iterator;
import java.util.List;

/**
 * Created by Junaid on 03-Feb-17.
 */


@PluginTestStep(typeName = "RegressorTestStep", name = "Regressor", description = "Compares all requests responses")
public class RegressorTestStep extends WsdlTestStepWithProperties {

    public static final String ICON = "compare.png";

    public RegressorTestStep(WsdlTestCase testCase, TestStepConfig config, boolean forLoadTest) {
        super(testCase, config, true, forLoadTest);
        if( !forLoadTest )
        {
            setIcon( UISupport.createImageIcon(ICON) );
        }

    }

    @Override
    public TestStepResult run(TestCaseRunner testRunner, TestCaseRunContext testRunContext) {
        //SoapUI.log("bu!");

        return compareResponses(testRunContext);
    }

    private TestStepResult compareResponses(TestCaseRunContext testCaseRunContext) {
        TestStepResult.TestStepStatus testStepStatus = TestStepResult.TestStepStatus.FAILED;
        StringBuilder buffer =new StringBuilder();
        List<String> responses = new ArrayList<>();

        RegressorTestStepResult result = new RegressorTestStepResult(this);
        List<TestStepResult> testRunnerResults = testCaseRunContext.getTestRunner().getResults();
        Iterator<TestStepResult> iterator = testRunnerResults.iterator();

        while(iterator.hasNext()){
            TestStepResult testStepResult = iterator.next();
            if(testStepResult instanceof WsdlTestRequestStepResult){
                buffer.append(testStepResult.getTestStep().getName());
                responses.add(((WsdlTestRequestStepResult) testStepResult).getResponseContent());
                result.addtestRequestStepResult((WsdlTestRequestStepResult) testStepResult);
                iterator.remove();
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
            }
        }

        result.addMessage("Grabbed Responses : " + responses);
        result.setStatus(testStepStatus);
        return result;
    }
}


