package org.hucompute.textimager.uima.base;

import org.apache.uima.UimaContext;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.resource.ResourceInitializationException;

import jep.JepException;
import jep.MainInterpreter;
import jep.PyConfig;
import jep.SharedInterpreter;

public abstract class JepAnnotator extends JCasAnnotator_ImplBase {
	/**
	 * The Python home directory
	 */
	public static final String PARAM_PYTHON_HOME = "pythonHome";
	@ConfigurationParameter(name = PARAM_PYTHON_HOME, mandatory = false)
	protected String pythonHome;

	/**
	 * The path to libjep lib
	 */
	public static final String PARAM_LIBJEP_PATH = "libjepPath";
	@ConfigurationParameter(name = PARAM_LIBJEP_PATH, mandatory = false)
	protected String libjepPath;

	protected SharedInterpreter interp;

	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);

		try {
			if (!pythonHome.isEmpty()) {
				PyConfig config = new PyConfig();
				config.setPythonHome(pythonHome);
				MainInterpreter.setInitParams(config);
			}

			if (libjepPath!= null && !libjepPath.isEmpty()) {
				MainInterpreter.setJepLibraryPath(libjepPath);
			}

			interp = new SharedInterpreter();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new ResourceInitializationException(ex);
		}
	}

	@Override
	public void destroy() {
		try {
			interp.close();
		} catch (JepException e) {
			e.printStackTrace();
		}

		super.destroy();
	}
}