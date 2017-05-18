/*

Chunker divides a sentence into syntactically 
correlated parts of words, like noun groups, verb groups, etc.,

*/
package com.igate.iv3.unstructured;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import opennlp.tools.chunker.ChunkSample;
import opennlp.tools.chunker.ChunkSampleStream;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Sequence;
import opennlp.tools.util.TrainingParameters;

import org.apache.hadoop.fs.Path;

public class Chunker {

	private ChunkerME chunker = null;

	Chunker(String chunker_model) {

		get_chunker(chunker_model);
	}
	
	Chunker(Path chunker_model) {

		get_chunker(chunker_model);
	}	
	
	Chunker(String lang, String char_set, String training_data, String chunker_model) {

		train_model(lang, char_set, training_data, chunker_model);
		get_chunker(chunker_model);
	}
	
	Chunker(String lang, String char_set, Path training_data, Path chunker_model) {

		train_model(lang, char_set, training_data, chunker_model);
		get_chunker(chunker_model);
	}	
	
	private void get_chunker(String chunker_model) {

		InputStream stream = null;
		ChunkerModel model = null;
		try {
			stream = new FileInputStream(chunker_model);
			model = new ChunkerModel(stream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();					
			}
		}
		chunker = new ChunkerME(model);
	}
	
	private void get_chunker(Path chunker_model) {

		InputStream stream = null;
		ChunkerModel model = null;
		try {
			stream = new FileInputStream(chunker_model.toString());
			model = new ChunkerModel(stream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();					
			}
		}
		chunker = new ChunkerME(model);
	}	
	
	@SuppressWarnings("deprecation")
	private void train_model(String lang, String char_set, String training_data, String chunker_model) {

		ObjectStream<String> line_stream = null;
		ObjectStream<ChunkSample> sample_stream = null;
		OutputStream model_stream = null;
		try {
			line_stream = new PlainTextByLineStream(new FileInputStream(training_data), Charset.forName(char_set));
			sample_stream = new ChunkSampleStream(line_stream);
			ChunkerModel model = ChunkerME.train(lang, sample_stream, null, TrainingParameters.defaultParams());
			model_stream = new BufferedOutputStream(new FileOutputStream(chunker_model));
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
	private void train_model(String lang, String char_set, Path training_data, Path chunker_model) {

		ObjectStream<String> line_stream = null;
		ObjectStream<ChunkSample> sample_stream = null;
		OutputStream model_stream = null;
		try {
			line_stream = new PlainTextByLineStream(new FileInputStream(training_data.toString()), Charset.forName(char_set));
			sample_stream = new ChunkSampleStream(line_stream);
			ChunkerModel model = ChunkerME.train(lang, sample_stream, null, TrainingParameters.defaultParams());
			model_stream = new BufferedOutputStream(new FileOutputStream(chunker_model.toString()));
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

	public String[] get_best_chunks(String[] tokens, String[] tags) {

		String[] chunks = chunker.chunk(tokens, tags);
		return chunks;
	}

	public Sequence[] get_all_chunks(String[] tokens, String[] tags) {

		Sequence[] sequences = chunker.topKSequences(tokens, tags);
		return sequences;
	}
}


