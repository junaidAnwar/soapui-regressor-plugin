package com.junaid.plugins;

import com.eviware.soapui.impl.EmptyPanelBuilder;
import com.eviware.soapui.model.PanelBuilder;
import com.eviware.soapui.model.util.PanelBuilderFactory;
import com.eviware.soapui.ui.desktop.DesktopPanel;

/**
 * Creates the DesktopPanel - could be extended to also support the bottom left Properties tab
 */

public class RegressorTestStepPanelBuilderFactory implements PanelBuilderFactory<RegressorTestStep>
{
	@Override
	public PanelBuilder<RegressorTestStep> createPanelBuilder()
	{
		return new RegressorTestStepPanelBuilder();
	}

	@Override
	public Class<RegressorTestStep> getTargetModelItem()
	{
		return RegressorTestStep.class;
	}

	public static class RegressorTestStepPanelBuilder extends EmptyPanelBuilder<RegressorTestStep>
	{
		@Override
		public DesktopPanel buildDesktopPanel( RegressorTestStep modelItem )
		{
			return new RegressorTestStepDesktopPanel( modelItem );
		}

		@Override
		public boolean hasDesktopPanel()
		{
			return true;
		}
	}
}
