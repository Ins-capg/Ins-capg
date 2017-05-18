/*

The name finder identifies user specified entity 
from a corpus. Any kind of entity could be identified 
by a context specific training of a model and using it. 
An entity could be person names, location, medical terminologies, 
etc.,

*/
package com.igate.iv3.unstructured;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collections;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;

import org.apache.hadoop.fs.Path;

public class NameFinder {
	
	private NameFinderME finder = null;
	
	NameFinder(String name_finder_model) {
		
		get_finder(name_finder_model);
	}
	
	NameFinder(Path name_finder_model) {
		
		get_finder(name_finder_model);
	}	

	NameFinder(String lang, String char_set, String entity, String training_data, String name_finder_model) {
		
		train_model(lang, char_set, entity, training_data, name_finder_model);
		get_finder(name_finder_model);
	}
	
	NameFinder(String lang, String char_set, String entity, Path training_data, Path name_finder_model) {
		
		train_model(lang, char_set, entity, training_data, name_finder_model);
		get_finder(name_finder_model);
	}	

	private void get_finder(String name_finder_model) {
		
		InputStream stream = null;
		TokenNameFinderModel model = null;
		try {
			stream = new FileInputStream(name_finder_model);
		    model = new TokenNameFinderModel(stream);
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		    	stream.close();
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
		}
		finder = new NameFinderME(model);
	}

	private void get_finder(Path name_finder_model) {
		
		InputStream stream = null;
		TokenNameFinderModel model = null;
		try {
			stream = new FileInputStream(name_finder_model.toString());
		    model = new TokenNameFinderModel(stream);
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		    	stream.close();
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
		}
		finder = new NameFinderME(model);
	}

	private void train_model(String lang, String char_set, String entity, String training_data, String name_finder_model) {
		
		ObjectStream<String> line_stream = null;
		ObjectStream<NameSample> sample_stream = null;
		OutputStream model_stream = null;
		try {
			line_stream = new PlainTextByLineStream(new FileInputStream(training_data), Charset.forName(char_set));
			sample_stream = new NameSampleDataStream(line_stream);			
			TokenNameFinderModel model = NameFinderME.train(lang, entity, sample_stream, Collections.<String, Object>emptyMap());
			model_stream = new BufferedOutputStream(new FileOutputStream(name_finder_model));
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

	private void train_model(String lang, String char_set, String entity, Path training_data, Path name_finder_model) {
		
		ObjectStream<String> line_stream = null;
		ObjectStream<NameSample> sample_stream = null;
		OutputStream model_stream = null;
		try {
			line_stream = new PlainTextByLineStream(new FileInputStream(training_data.toString()), Charset.forName(char_set));
			sample_stream = new NameSampleDataStream(line_stream);			
			TokenNameFinderModel model = NameFinderME.train(lang, entity, sample_stream, Collections.<String, Object>emptyMap());
			model_stream = new BufferedOutputStream(new FileOutputStream(name_finder_model.toString()));
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

	public Span[] get_spans(String[] tokens) {

		Span[] spans = finder.find(tokens);
		return spans;
	}
	
	public String[] get_names(String[] tokens) {
		
		Span[] spans = get_spans(tokens);
		String[] names = new String[spans.length];
		if (spans.length > 0) {
	    	for (int i=0; i<spans.length; i++) {
	    		StringBuilder builder = new StringBuilder();	    		
	    		for (int j=spans[i].getStart(); j<spans[i].getEnd(); j++) { 
	    			builder.append(tokens[j] + " ");
	    		}
	    		names[i] = (builder.toString().trim());
	    	}
	    }
		return names;
	}	
}

