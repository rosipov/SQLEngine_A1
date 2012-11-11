#!/usr/bin/env python
import sys, os, re

just_consume = ['Semicolon', 'Comma', 'OpenParen', 'CloseParen', 'Asterisk']

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
	print 'public ASTNode parse%s() throws ParseError {' % name
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
				print 'try { return parse%s(); } catch (MaybeParseError e) {}' % thing
			else:
				print 'try {'
				if thing.generate.func_code.co_argcount == 2:
					thing.generate(name)
				else:
					thing.generate()
				print '} catch (MaybeParseError e) {}'
		print 'throw new MaybeParseError("expected one of %s, next token is " + tokens.get(position));' % repr(
			[(t if isinstance(t, str) else t.first_thing()) for t in self.things])

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
					print 'catch (MaybeParseError e) { position = savePos; throw new DefiniteParseError(e.getMessage()); }'
				else:
					print 'catch (MaybeParseError e) { position = savePos; throw e; }'
		print 'return rv;'
	
	def first_thing(self):
		return self.things[0][0] if isinstance(self.things[0], tuple) else self.things[0]

class Keyword:
	def __init__(self, kw):
		self.kw = kw
	
	def generate(self):
		print 'Token t = tokens.get(position);'
		print 'if (t.type == Token.Type.KEYWORD && t.text.equals("%s")) {' % self.kw
		print 'position++;'
		print 'return new ASTNode(ASTNode.Type.%s, "%s"); }' % (self.kw, self.kw)
		print 'else throw new MaybeParseError("expected %s, got " + t);' % self.kw

class Name:
	def generate(self):
		print 'Token t = tokens.get(position);'
		print 'if (t.type == Token.Type.NAME) {'
		print 'position++;'
		print 'return new ASTNode(ASTNode.Type.NAME, t.text); }'
		print 'else throw new MaybeParseError("expected name, got " + t);'

class StaticTerminal:
	def __init__(self, name):
		self.name = name
	
	def generate(self):
		print 'Token t = tokens.get(position);'
		print 'if (t.type == Token.Type.%s) { position++; return null; }' % constify_name(self.name)
		print 'else throw new MaybeParseError("expected %s, got " + t);' % self.name

class Name:
	def generate(self):
		print 'Token t = tokens.get(position);'
		print 'if (t.type == Token.Type.NAME) { position++; return new ASTNode(ASTNode.Type.NAME, t.text); }'
		print 'else throw new MaybeParseError("expected name, got " + t);'

class Integer:
	def generate(self):
		print 'Token t = tokens.get(position);'
		print 'if (t.type == Token.Type.INTEGER) { position++; return new ASTNode(ASTNode.Type.INTEGER, Integer.parseInt(t.text)); }'
		print 'else throw new MaybeParseError("expected integer, got " + t);'

class String:
	def generate(self):
		print 'Token t = tokens.get(position);'
		print 'if (t.type == Token.Type.STRING) { position++; return new ASTNode(ASTNode.Type.STRING, t.text); }'
		print 'else throw new MaybeParseError("expected string, got " + t);'

class Float:
	def generate(self):
		print 'Token t = tokens.get(position);'
		print 'if (t.type == Token.Type.FLOAT) { position++; return new ASTNode(ASTNode.Type.FLOAT, Float.parseFloat(t.text)); }'
		print 'else throw new MaybeParseError("expected float, got " + t);'
		
class Epsilon:
	def generate(self):
		print 'return null;'

prelude()

rule('Statement', OneOf('InsertStatement', 'SelectStatement'))

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
rule('ParenthesizedInsertColumnList', Sequence('OpenParen', ('ColumnList', 'columns'), 'CloseParen'))
rule('ColumnList', OneOf(
	Sequence(('Name', 'this'), 'Comma', ('ColumnList', 'next')),
	'Name'))
rule('InsertRowList', OneOf(
	Sequence(('ParenthesizedInsertRow', 'this'), 'Comma', ('InsertRowList', 'next')),
	'ParenthesizedInsertRow'))
rule('ParenthesizedInsertRow', Sequence('OpenParen', ('InsertRow', 'values'), 'CloseParen'))
rule('InsertRow', OneOf(
	Sequence(('Expression', 'this'), 'Comma', ('InsertRow', 'next')),
	'Expression'))

rule('SelectStatement', Sequence(
	'SelectKeyword',
	Definite(),
	('MaybeColumnSet', 'columns'),
	'FromKeyword',
	('Name', 'table-name'),
	'Semicolon'))
rule('MaybeColumnSet', OneOf('ColumnList', 'Asterisk'))

rule('Expression', OneOf('String', 'Integer', 'Float', 'Name'))

for kw in ("INSERT", "SELECT", "UPDATE", "DROP", "DELETE", "CREATE", "INTO", "FROM", "VALUES", "TABLE", "SAVE", "COMMIT", "LOAD", "DATABASE"):
	rule(kw.title() + 'Keyword', Keyword(kw))
for terminal in ('Semicolon', 'Comma', 'OpenParen', 'CloseParen', 'Asterisk'):
	rule(terminal, StaticTerminal(terminal))
for terminal in ('Name', 'Integer', 'String', 'Float', 'Epsilon'):
	rule(terminal, globals()[terminal]())

postlude()