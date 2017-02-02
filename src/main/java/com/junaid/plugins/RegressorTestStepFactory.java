package com.junaid.plugins;

import com.eviware.soapui.config.TestStepConfig;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestStep;
import com.eviware.soapui.impl.wsdl.teststeps.registry.WsdlTestStepFactory;

/**
 * The actual factory class that creates new EMailTestSteps from scratch or an XMLBeans config.
 */

public class RegressorTestStepFactory extends WsdlTestStepFactory
{
	private static final String REGRESSOR_STEP_ID = "regressor";

	public RegressorTestStepFactory()
	{
		super(REGRESSOR_STEP_ID, "Regressor TestStep", "Compares responses from all requests", "email.png" );
	}

	public WsdlTestStep buildTestStep( WsdlTestCase testCase, TestStepConfig config, boolean forLoadTest )
	{
		return new RegressorTestStep( testCase, config, forLoadTest );
	}

	public TestStepConfig createNewTestStep( WsdlTestCase testCase, String name )
	{
		TestStepConfig testStepConfig = TestStepConfig.Factory.newInstance();
		testStepConfig.setType(REGRESSOR_STEP_ID);
		testStepConfig.setName( name );
		return testStepConfig;
	}

	public boolean canCreate()
	{
		return true;
	}
}
