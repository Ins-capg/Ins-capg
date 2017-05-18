/*

Sentence Detector splits the input corpus into sentences.
It requires the user to specify an existing model or a 
training dataset to create a model on the fly.

*/
package com.igate.iv3.unstructured;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.sentdetect.SentenceSample;
import opennlp.tools.sentdetect.SentenceSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;
import opennlp.tools.util.TrainingParameters;

import org.apache.hadoop.fs.Path;

public class SentenceDetector {

	private SentenceDetectorME detector = null;

	SentenceDetector(String sentence_detector_model) {
		
		get_detector(sentence_detector_model);
	}

	SentenceDetector(Path sentence_detector_model) {
		
		get_detector(sentence_detector_model);
	}
	
	SentenceDetector(String lang, String char_set, String training_data, String sentence_detector_model) {
		
		train_model(lang, char_set, training_data, sentence_detector_model);
		get_detector(sentence_detector_model);
	}
	
	SentenceDetector(String lang, String char_set, Path training_data, Path sentence_detector_model) {
		
		train_model(lang, char_set, training_data, sentence_detector_model);
		get_detector(sentence_detector_model);
	}
	
	private void get_detector(String sentence_detector_model) {
		
		InputStream stream = null;
		SentenceModel model = null;
		try {
			stream = new FileInputStream(sentence_detector_model);
			model = new SentenceModel(stream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();					
			}
		}
		detector = new SentenceDetectorME(model);
	}
	
	private void get_detector(Path sentence_detector_model) {
		
		InputStream stream = null;
		SentenceModel model = null;
		try {
			stream = new FileInputStream(sentence_detector_model.toString());
			model = new SentenceModel(stream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();					
			}
		}
		detector = new SentenceDetectorME(model);
	}	
	
	@SuppressWarnings("deprecation")
	private void train_model(String lang, String char_set, String training_data, String sentence_detector_model) {
		
		ObjectStream<String> line_stream = null;
		ObjectStream<SentenceSample> sample_stream = null;
		OutputStream model_stream = null;
		try {
			line_stream = new PlainTextByLineStream(new FileInputStream(training_data), Charset.forName(char_set));
			sample_stream = new SentenceSampleStream(line_stream);
			SentenceModel model = SentenceDetectorME.train(lang, sample_stream, true, null, TrainingParameters.defaultParams());
			model_stream = new BufferedOutputStream(new FileOutputStream(sentence_detector_model));
			model.serialize(model_stream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				model_stream.close();
				sample_stream.close();
				line_stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void train_model(String lang, String char_set, Path training_data, Path sentence_detector_model) {
		
		ObjectStream<String> line_stream = null;
		ObjectStream<SentenceSample> sample_stream = null;
		OutputStream model_stream = null;
		try {
			line_stream = new PlainTextByLineStream(new FileInputStream(training_data.toString()), Charset.forName(char_set));
			sample_stream = new SentenceSampleStream(line_stream);
			SentenceModel model = SentenceDetectorME.train(lang, sample_stream, true, null, TrainingParameters.defaultParams());
			model_stream = new BufferedOutputStream(new FileOutputStream(sentence_detector_model.toString()));
			model.serialize(model_stream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				model_stream.close();
				sample_stream.close();
				line_stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	

	public String[] get_sentences(String corpus) {
		
		String[] sentences = detector.sentDetect(corpus);
		return sentences;
	}
	
	public Span[] get_spans(String corpus) {
		
		Span[] spans = detector.sentPosDetect(corpus);
		return spans;
	}
}
