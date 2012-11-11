#!/usr/bin/env python
import sys, os, re

just_consume = ['Semicolon', 'Comma', 'OpenParen', 'CloseParen']

def prelude():
	print """
package sqlengine_a1.parser;
import java.util.*;

public class SqlParser {
	private Vector<Token> tokens;
	private int position;
	
	public SqlParser(String str) throws ParseError {
		tokens = new SqlTokenizer(str).getAllTokens();
		position = 0;
	}
"""

def postlude():
	print '}'

def constify_name(name):
	return re.sub('([a-z])([A-Z])', r'\1_\2', name).upper()

def rule(name, thing):
	print 'public %s parse%s() throws ParseError {' % ('void' if name in just_consume else 'ASTNode', name)
	if thing.generate.func_code.co_argcount == 2:
		thing.generate(constify_name(name))
	else:
		thing.generate()
	print '}'

class OneOf:
	def __init__(self, *args):
		self.things = args
	
	def generate(self, name):
		for thing in self.things:
			if isinstance(thing, str):
				print 'try { return parse%s(); } catch (ParseError e) {}' % thing
			else:
				print 'try {'
				if thing.generate.func_code.co_argcount == 2:
					thing.generate(name)
				else:
					thing.generate()
				print '} catch (ParseError e) {}'
		print 'throw new ParseError("expected one of %s, next token is " + tokens.get(position));' % repr(self.things)

class Definite:
	# marker for use inside Sequence. if parsing gets this far, a failure later in the sequence is definitely an error.
	pass

class Sequence:
	def __init__(self, *args):
		self.things = args
	
	def generate(self, rule_name):
		print 'int savePos = position;'
		print 'ASTNode rv = new ASTNode(ASTNode.Type.%s);' % rule_name
		definite = False
		for thing in self.things:
			if isinstance(thing, Definite):
				definite = True
			else:
				if isinstance(thing, tuple):
					thing, thing_name = thing
				else:
					thing_name = None
				
				if thing in just_consume or thing_name is None:
					print 'try { parse%s(); }' % thing
				else:
					print 'try { ASTNode temp = parse%s(); if (temp != null) rv.subnodes.put("%s", temp); }' % (thing, thing_name)
				
				if definite:
					print 'catch (ParseError e) { position = savePos; throw e; }'
				else:
					print 'catch (ParseError e) { position = savePos; throw e; }'
		print 'return rv;'

class Keyword:
	def __init__(self, kw):
		self.kw = kw
	
	def generate(self):
		print 'Token t = tokens.get(position);'
		print 'if (t.type == Token.Type.KEYWORD && t.text.equals("%s")) {' % self.kw
		print 'position++;'
		print 'return new ASTNode(ASTNode.Type.%s, "%s"); }' % (self.kw, self.kw)
		print 'else throw new ParseError("expected %s, got " + t);' % self.kw

class Name:
	def generate(self):
		print 'Token t = tokens.get(position);'
		print 'if (t.type == Token.Type.NAME) {'
		print 'position++;'
		print 'return new ASTNode(ASTNode.Type.NAME, t.text); }'
		print 'else throw new ParseError("expected name, got " + t);'

class Semicolon:
	def generate(self):
		print 'Token t = tokens.get(position);'
		print 'if (t.type == Token.Type.SEMICOLON) { position++; }'
		print 'else throw new ParseError("expected semicolon, got " + t);'

class Comma:
	def generate(self):
		print 'Token t = tokens.get(position);'
		print 'if (t.type == Token.Type.COMMA) { position++; }'
		print 'else throw new ParseError("expected comma, got " + t);'

class OpenParen:
	def generate(self):
		print 'Token t = tokens.get(position);'
		print 'if (t.type == Token.Type.OPEN_PAREN) { position++; }'
		print 'else throw new ParseError("expected comma, got " + t);'

class CloseParen:
	def generate(self):
		print 'Token t = tokens.get(position);'
		print 'if (t.type == Token.Type.CLOSE_PAREN) { position++; }'
		print 'else throw new ParseError("expected comma, got " + t);'

class Integer:
	def generate(self):
		print 'Token t = tokens.get(position);'
		print 'if (t.type == Token.Type.INTEGER) { position++; return new ASTNode(ASTNode.Type.INTEGER, Integer.parseInt(t.text)); }'
		print 'else throw new ParseError("expected comma, got " + t);'

class String:
	def generate(self):
		print 'Token t = tokens.get(position);'
		print 'if (t.type == Token.Type.STRING) { position++; return new ASTNode(ASTNode.Type.STRING, t.text); }'
		print 'else throw new ParseError("expected comma, got " + t);'

class Float:
	def generate(self):
		print 'Token t = tokens.get(position);'
		print 'if (t.type == Token.Type.FLOAT) { position++; return new ASTNode(ASTNode.Type.FLOAT, Float.parseFloat(t.text)); }'
		print 'else throw new ParseError("expected comma, got " + t);'
		
class Epsilon:
	def generate(self):
		print 'return null;'

prelude()

rule('Statement', OneOf('InsertStatement'))
rule('InsertStatement', Sequence(
	'InsertKeyword',
	Definite(),
	'IntoKeyword',
	('Name', 'table-name'),
	('MaybeInsertColumnList', 'columns'),
	'ValuesKeyword',
	('InsertRowList', 'rows'),
	'Semicolon'))
rule('MaybeInsertColumnList', OneOf('ParenthesizedInsertColumnList', 'Epsilon'))
rule('ParenthesizedInsertColumnList', Sequence('OpenParen', ('InsertColumnList', 'columns'), 'CloseParen'))
rule('InsertColumnList', OneOf(
	Sequence(('Name', 'this'), 'Comma', ('InsertColumnList', 'next')),
	'Name'))

rule('InsertRowList', OneOf(
	Sequence(('ParenthesizedInsertRow', 'this'), 'Comma', ('InsertRowList', 'next')),
	'ParenthesizedInsertRow'))

rule('ParenthesizedInsertRow', Sequence('OpenParen', ('InsertRow', 'values'), 'CloseParen'))
rule('InsertRow', OneOf(
	Sequence(('Expression', 'this'), 'Comma', ('InsertRow', 'next')),
	'Expression'))

rule('Expression', OneOf('String', 'Integer', 'Float', 'Name'))

for kw in ("INSERT", "SELECT", "UPDATE", "DROP", "DELETE", "CREATE", "INTO", "FROM", "VALUES", "TABLE", "SAVE", "COMMIT", "LOAD", "DATABASE"):
	rule(kw.title() + 'Keyword', Keyword(kw))
for terminal in ('Name', 'Semicolon', 'Comma', 'OpenParen', 'CloseParen', 'Integer', 'String', 'Float', 'Epsilon'):
	rule(terminal, globals()[terminal]())

postlude()