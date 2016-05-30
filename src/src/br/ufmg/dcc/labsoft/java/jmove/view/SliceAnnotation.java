package br.ufmg.dcc.labsoft.java.jmove.view;

import org.eclipse.jface.text.source.Annotation;

public class SliceAnnotation extends Annotation {
	public static final String EXTRACTION = "br.ufmg.dcc.labsoft.java.jmove.extractionAnnotation";

	public SliceAnnotation(String type, String text) {
		super(type, false, text);
	}
}
