/*

The part of speech tagger marks tokens with 
their corresponding word type based on the token 
itself and the context of the token. A token might 
have multiple pos tags depending on the token and 
the context.

*/
package com.igate.iv3.unstructured;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.postag.WordTagSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Sequence;
import opennlp.tools.util.TrainingParameters;

import org.apache.hadoop.fs.Path;

public class PosTagger {

	private POSTaggerME tagger = null;
	
	PosTagger(String pos_tagger_model) {

		get_tagger(pos_tagger_model);
	}
	
	PosTagger(Path pos_tagger_model) {

		get_tagger(pos_tagger_model);
	}
	
	PosTagger(String lang, String char_set, String training_data, String pos_tagger_model) {

		train_model(lang, char_set, training_data, pos_tagger_model);
		get_tagger(pos_tagger_model);
	}

	PosTagger(String lang, String char_set, Path training_data, Path pos_tagger_model) {

		train_model(lang, char_set, training_data, pos_tagger_model);
		get_tagger(pos_tagger_model);
	}	

	private void get_tagger(String pos_tagger_model) {

		InputStream stream = null;
		POSModel model = null;
		try {
			stream = new FileInputStream(pos_tagger_model);
			model = new POSModel(stream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();					
			}
		}
		tagger = new POSTaggerME(model);
	}
	
	private void get_tagger(Path pos_tagger_model) {

		InputStream stream = null;
		POSModel model = null;
		try {
			stream = new FileInputStream(pos_tagger_model.toString());
			model = new POSModel(stream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();					
			}
		}
		tagger = new POSTaggerME(model);
	}
	
	@SuppressWarnings("deprecation")
	private void train_model(String lang, String char_set, String training_data, String pos_tagger_model) {
		
		ObjectStream<String> line_stream = null;
		ObjectStream<POSSample> sample_stream = null;
		OutputStream model_stream = null;
		try {
			line_stream = new PlainTextByLineStream(new FileInputStream(training_data), Charset.forName(char_set));
			sample_stream = new WordTagSampleStream(line_stream);
			POSModel model = POSTaggerME.train(lang, sample_stream, TrainingParameters.defaultParams(), null, null);
			model_stream = new BufferedOutputStream(new FileOutputStream(pos_tagger_model));
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
	private void train_model(String lang, String char_set, Path training_data, Path pos_tagger_model) {
		
		ObjectStream<String> line_stream = null;
		ObjectStream<POSSample> sample_stream = null;
		OutputStream model_stream = null;
		try {
			line_stream = new PlainTextByLineStream(new FileInputStream(training_data.toString()), Charset.forName(char_set));
			sample_stream = new WordTagSampleStream(line_stream);
			POSModel model = POSTaggerME.train(lang, sample_stream, TrainingParameters.defaultParams(), null, null);
			model_stream = new BufferedOutputStream(new FileOutputStream(pos_tagger_model.toString()));
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

	public String[] get_best_tags(String[] tokens) {

		String[] tags = tagger.tag(tokens);
		return tags;
	}
	
	public Sequence[] get_all_tags(String[] tokens) {

		Sequence[] sequences = tagger.topKSequences(tokens);
		return sequences;
	}
}
