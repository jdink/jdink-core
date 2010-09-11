package de.siteof.jdink.view;

import de.siteof.jdink.model.JDinkContext;

public class AbstractJDinkView implements JDinkView {

	@Override
	public JDinkImageFactory getImageLoader() {
		return null;
	}

	@Override
	public void init(JDinkContext context, Object viewInitParameter) {
	}

	@Override
	public void onBeforeLoad(JDinkContext context) {
	}

	@Override
	public boolean isStopping() {
		return false;
	}

	@Override
	public void setSplashImage(JDinkImage image) {
	}

	@Override
	public void updateView() {
	}

	@Override
	public void waitForView(long timeout) {
	}

}
