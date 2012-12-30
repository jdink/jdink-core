package de.siteof.jdink.view;

import de.siteof.jdink.model.JDinkContext;


public interface JDinkView {

	void init(JDinkContext context, Object viewInitParameter);

	void onBeforeLoad(JDinkContext context);

	void setSplashImage(JDinkImage image);

	void updateView();

	void waitForView(long timeout);

	JDinkImageFactory getImageLoader();

	boolean isStopping();

}
