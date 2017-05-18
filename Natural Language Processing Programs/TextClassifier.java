/*

Text Classifier component classifies 
text into predefined categories.

*/


package com.igate.iv3.unstructured;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

import org.apache.hadoop.fs.Path;

public class TextClassifier {

	private DocumentCategorizerME categorizer = null;

	TextClassifier(String text_classifier_model) {
		
		get_categorizer(text_classifier_model);
	}	
	
	TextClassifier(Path text_classifier_model) {
		
		get_categorizer(text_classifier_model);
	}	
	
	TextClassifier(String lang, String char_set, String training_data, String text_classifier_model) {
	
		train_model(lang, char_set, training_data, text_classifier_model);
		get_categorizer(text_classifier_model);
	}
	
	TextClassifier(String lang, String char_set, Path training_data, Path text_classifier_model) {
		
		train_model(lang, char_set, training_data, text_classifier_model);
		get_categorizer(text_classifier_model);
	}	
	
	private void get_categorizer(String text_classifier_model) {        
		
		InputStream stream = null;
		DoccatModel model = null;
		try {
			stream = new FileInputStream(text_classifier_model);
			model = new DoccatModel(stream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		categorizer = new DocumentCategorizerME(model);
	}
	
	private void get_categorizer(Path text_classifier_model) {        
		
		InputStream stream = null;
		DoccatModel model = null;
		try {
			stream = new FileInputStream(text_classifier_model.toString());
			model = new DoccatModel(stream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		categorizer = new DocumentCategorizerME(model);
	}	
	
	private void train_model(String lang, String char_set, String training_data, String text_classifier_model) {
		
		ObjectStream<String> line_stream = null;
		ObjectStream<DocumentSample> sample_stream = null;
		OutputStream model_stream = null;
		try {
			line_stream = new PlainTextByLineStream(new FileInputStream(training_data), Charset.forName(char_set));
			sample_stream = new DocumentSampleStream(line_stream);
			DoccatModel model = DocumentCategorizerME.train(lang, sample_stream, TrainingParameters.defaultParams());
			model_stream = new BufferedOutputStream(new FileOutputStream(text_classifier_model));
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

	private void train_model(String lang, String char_set, Path training_data, Path text_classifier_model) {
		
		ObjectStream<String> line_stream = null;
		ObjectStream<DocumentSample> sample_stream = null;
		OutputStream model_stream = null;
		try {
			line_stream = new PlainTextByLineStream(new FileInputStream(training_data.toString()), Charset.forName(char_set));
			sample_stream = new DocumentSampleStream(line_stream);
			DoccatModel model = DocumentCategorizerME.train(lang, sample_stream, TrainingParameters.defaultParams());
			model_stream = new BufferedOutputStream(new FileOutputStream(text_classifier_model.toString()));
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
	
	public String[] get_all_categories(String corpus) {
		
		double[] outcomes = categorizer.categorize(corpus);
		return categorizer.getAllResults(outcomes).split(" ");
	}
	
	public String[] get_all_categories(String[] sentences) {
		
		double[] outcomes = categorizer.categorize(sentences);
		return categorizer.getAllResults(outcomes).split(" ");
	}
	
	public String get_best_category(String corpus) {
		
		double[] outcomes = categorizer.categorize(corpus);
		return categorizer.getBestCategory(outcomes);
	}
	
	public String get_best_category(String[] sentences) {
		
		double[] outcomes = categorizer.categorize(sentences);
		return categorizer.getBestCategory(outcomes);
	}
}

