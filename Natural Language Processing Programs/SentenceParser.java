/*

Sentence Parser parses an input sentence into
a parse tree which enables the user to understand
the syntactical structure of the sentence.

*/
package com.igate.iv3.unstructured;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.parser.chunking.Parser;

import org.apache.hadoop.fs.Path;

public class SentenceParser {

	private Parser parser = null;
	private String phrase_tag = null;
	private List<Parse> phrases = new ArrayList<Parse>();

	SentenceParser(String sentence_parser_model) {
		
		get_parser(sentence_parser_model);
	}

	SentenceParser(Path sentence_parser_model) {
		
		get_parser(sentence_parser_model);
	}	
	
	private void get_parser(String sentence_parser_model) {
		
		InputStream stream = null;
		ParserModel model = null;
		try {
			stream = new FileInputStream(sentence_parser_model);
			model = new ParserModel(stream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();					
			}
		}
		this.parser = (Parser) ParserFactory.create(model);
	}

	private void get_parser(Path sentence_parser_model) {
		
		InputStream stream = null;
		ParserModel model = null;
		try {
			stream = new FileInputStream(sentence_parser_model.toString());
			model = new ParserModel(stream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();					
			}
		}
		this.parser = (Parser) ParserFactory.create(model);
	}	
	
	private void set_phrase_tag(String phrase_tag) {
		
		this.phrase_tag = phrase_tag;
	}
	
	private void add_phrases(Parse parse) {
		
		if (parse.getType().contains(this.phrase_tag)) {
			this.phrases.add(parse);
		}
		for (Parse child : parse.getChildren()) {
			add_phrases(child);
		}
	}

	public Parse get_best_parse(String sentence) {
		
		Parse[] parses = ParserTool.parseLine(sentence, this.parser, 1);
		return parses[0];
	}
	
	public List<Parse> get_phrases(String sentence, String phrase_tag) {
		
		set_phrase_tag(phrase_tag);
		Parse parse = get_best_parse(sentence);
		add_phrases(parse);
		return this.phrases;
	}
	
	public Parse[] get_n_parses(String sentence, int num_parses) {
		
		Parse[] parses = ParserTool.parseLine(sentence, this.parser, num_parses);
		return parses;
	}
	
	public List<Parse> get_phrases(String sentence, int num_parses, String phrase_tag) {
		
		set_phrase_tag(phrase_tag);
		Parse[] parses = get_n_parses(sentence, num_parses);
		for (Parse parse : parses) {
			add_phrases(parse);
		}
		return this.phrases;
	}
}
