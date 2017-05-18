/* 

The Tokenizer splits input sentence into tokens. 
User can specify among Whitespace Tokenizer, 
Simple Tokenizer and Learnable Tokenizer depending on their need.

/*

package com.igate.iv3.unstructured;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.TokenSample;
import opennlp.tools.tokenize.TokenSampleStream;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;
import opennlp.tools.util.TrainingParameters;

import org.apache.hadoop.fs.Path;

public class Tokenizer {

	private opennlp.tools.tokenize.Tokenizer tokenizer = null;
	
	Tokenizer(String tokenizer_model) {
		
		get_tokenizer(tokenizer_model);
	}

	Tokenizer(Path tokenizer_model) {
		
		get_tokenizer(tokenizer_model);
	}
	
	Tokenizer(String lang, String char_set, String training_data, String tokenizer_model) {
		
		train_model(lang, char_set, training_data, tokenizer_model);
		get_tokenizer(tokenizer_model);
	}
	
	Tokenizer(String lang, String char_set, Path training_data, Path tokenizer_model) {
		
		train_model(lang, char_set, training_data, tokenizer_model);
		get_tokenizer(tokenizer_model);
	}
	
	private void get_tokenizer(String tokenizer_model) {        
		
		InputStream stream = null;
		TokenizerModel model = null;
		try {
			stream = new FileInputStream(tokenizer_model);
			model = new TokenizerModel(stream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		tokenizer = new TokenizerME(model);
	}

	private void get_tokenizer(Path tokenizer_model) {        
		
		InputStream stream = null;
		TokenizerModel model = null;
		try {
			stream = new FileInputStream(tokenizer_model.toString());
			model = new TokenizerModel(stream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		tokenizer = new TokenizerME(model);
	}
	
	@SuppressWarnings("deprecation")
	private void train_model(String lang, String char_set, String training_data, String tokenizer_model) {
		
		ObjectStream<String> line_stream = null;
		ObjectStream<TokenSample> sample_stream = null;
		OutputStream model_stream = null;
		try {
			line_stream = new PlainTextByLineStream(new FileInputStream(training_data), Charset.forName(char_set));
			sample_stream = new TokenSampleStream(line_stream);
			TokenizerModel model = TokenizerME.train(lang, sample_stream, true, TrainingParameters.defaultParams());
			model_stream = new BufferedOutputStream(new FileOutputStream(tokenizer_model));
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
	private void train_model(String lang, String char_set, Path training_data, Path tokenizer_model) {
		
		ObjectStream<String> line_stream = null;
		ObjectStream<TokenSample> sample_stream = null;
		OutputStream model_stream = null;
		try {
			line_stream = new PlainTextByLineStream(new FileInputStream(training_data.toString()), Charset.forName(char_set));
			sample_stream = new TokenSampleStream(line_stream);
			TokenizerModel model = TokenizerME.train(lang, sample_stream, true, TrainingParameters.defaultParams());
			model_stream = new BufferedOutputStream(new FileOutputStream(tokenizer_model.toString()));
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

	public String[] get_tokens(String sentence) {

		String[] tokens = tokenizer.tokenize(sentence);
		return tokens;
	}
	
	public Span[] get_spans(String sentence) {

		Span[] spans = tokenizer.tokenizePos(sentence);
		return spans;
	}
	
	public String[] get_tokens_by_whitespace(String sentence) {
		
		WhitespaceTokenizer tokenizer = WhitespaceTokenizer.INSTANCE;
		String[] tokens = tokenizer.tokenize(sentence);
		return tokens;
	}
	
	public Span[] get_spans_by_whitespace(String sentence) {
		
		WhitespaceTokenizer tokenizer = WhitespaceTokenizer.INSTANCE;
		Span[] spans = tokenizer.tokenizePos(sentence);
		return spans;
	}
	
	public String[] get_tokens_by_charstream(String sentence) {
		
		SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
		String[] tokens = tokenizer.tokenize(sentence);
		return tokens;
	}
	
	public Span[] get_spans_by_charstream(String sentence) {
		
		SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
		Span[] spans = tokenizer.tokenizePos(sentence);
		return spans;
	}
}
