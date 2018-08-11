package com.github.tgstation.fastdmm;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

import java.awt.Color;

public class TextAreaOutputStream extends OutputStream {
	private JTextArea textArea;
	private Color color;
	private String prefix;

	public TextAreaOutputStream(JTextArea textArea, String prefix, Color color) {
		this.textArea = textArea;
	}

	@Override
	public void write(int b) throws IOException {
		// redirects data to the text area
		textArea.append(String.valueOf((char) b));
		// scrolls the text area to the end of data
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}

}
